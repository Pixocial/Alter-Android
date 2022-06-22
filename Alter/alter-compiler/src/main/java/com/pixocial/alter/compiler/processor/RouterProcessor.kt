package com.pixocial.alter.compiler.processor

import com.pixocial.alter.annotation.Router
import com.pixocial.alter.compiler.AlterProcessor
import com.pixocial.alter.compiler.router.RouteConstants
import com.pixocial.alter.enums.RouteType
import com.pixocial.alter.model.RouteMeta
import com.squareup.javapoet.*
import java.io.IOException
import java.util.*
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.ElementFilter
import kotlin.collections.ArrayList

class RouterProcessor : BaseProcessor() {

    /**
     * key:组名 value:类名
     */
    private val mRootMap: MutableMap<String, String> = TreeMap()
    /**
     * 分组 key:组名 value:对应组的路由信息
     */
    private val mGroupMap: MutableMap<String, MutableList<RouteMeta>> = HashMap()

    override fun process(roundEnv: RoundEnvironment?, processingEnv: ProcessingEnvironment, mAbstractProcessor: AlterProcessor, moduleName: String) {
        super.process(roundEnv, processingEnv, mAbstractProcessor, moduleName)
        processRouter(roundEnv)
    }

    private fun processRouter(roundEnv: RoundEnvironment?) {
        ElementFilter.typesIn(roundEnv?.getElementsAnnotatedWith(Router::class.java)).forEach { element ->
            val asType = element!!.asType()
            val activity = mProcessor.mElementsUtils.getTypeElement(RouteConstants.ACTIVITY)
            val fragment = mProcessor.mElementsUtils.getTypeElement(RouteConstants.FRAGMENT)
            val fragmentV4 = mProcessor.mElementsUtils.getTypeElement(RouteConstants.FRAGMENT_V4)
            mProcessor.i("RouterProcessor Route class: $asType")

            val routeMeta: RouteMeta
            val router = element.getAnnotation(Router::class.java)
            if (mProcessor.mTypeUtils.isSubtype(asType, activity.asType())) {
                routeMeta = RouteMeta(RouteType.ACTIVITY, router, element)
                mProcessor.i("RouterProcessor Route activity class: $asType path:${routeMeta.path}")
            } else if (mProcessor.mTypeUtils.isSubtype(asType, fragment.asType())
                    || mProcessor.mTypeUtils.isSubtype(asType, fragmentV4.asType())) {
                routeMeta = RouteMeta(RouteType.FRAGMENT, router, element)
                mProcessor.i("RouterProcessor Route fragment class: $asType path:${routeMeta.path}")
            } else {
                throw RuntimeException("RouterProcessor just support Activity router: $element")
            }
            categories(routeMeta)
        }

        //获取接口
        val iRouteGroup = mProcessor.mElementsUtils.getTypeElement(RouteConstants.I_ROUTE_GROUP)
        val iRouteRoot = mProcessor.mElementsUtils.getTypeElement(RouteConstants.I_ROUTE_ROOT)
        genGroup(iRouteGroup)
        genRoot(iRouteRoot, iRouteGroup)
    }

    /**
     * 检查是否配置 group 如果没有配置 则从path截取出组名
     * @param routeMeta             routeMeta
     */
    private fun categories(routeMeta: RouteMeta) {
        if (routerVerify(routeMeta)) {
            var routerMetas = mGroupMap[routeMeta.group]
            if (routerMetas.isNullOrEmpty()) {
                routerMetas = ArrayList()
                routerMetas.add(routeMeta)
                mGroupMap[routeMeta.group] = routerMetas
            } else {
                routerMetas.add(routeMeta)
            }
        } else {
            throw RuntimeException("RouterProcessor: router path should be like /group/path, now it's group=${routeMeta.group} path=${routeMeta.path}")
        }
    }

    /**
     * 验证path路由地址的合法性
     * path路径必须是：path = "/test/TestActivity"   也就是  path = "/group/path"
     * @param routeMeta             routeMeta
     * @return                      true表示合法
     */
    private fun routerVerify(routeMeta: RouteMeta): Boolean {
        if (!routeMeta.path.startsWith("/")) {
            return false
        }
        if (routeMeta.group.isEmpty()) {
            val index = routeMeta.path.indexOf("/", 1)
            if (index < 1) {
                return false
            }
            val defaultGroup = routeMeta.path.substring(1, index)
            if (defaultGroup.isEmpty()) {
                return false
            }
            routeMeta.group = defaultGroup
        }
        return true
    }

    /**
     * 生成group分组表
     */
    private fun genGroup(iRouteGroup: TypeElement) {
        val parameterizedTypeName = ParameterizedTypeName.get(
                ClassName.get(Map::class.java),
                ClassName.get(String::class.java),
                ClassName.get(RouteMeta::class.java))
        val atlasParam = ParameterSpec.builder(parameterizedTypeName, "atlas").build()

        mGroupMap.entries.forEach { entry ->
            val methodBuilder = MethodSpec.methodBuilder(RouteConstants.METHOD_LOAD_INTO)
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override::class.java)
                    .addParameter(atlasParam)

            val groupName = entry.key
            val groupData = entry.value
            groupData.forEach { routeMeta ->
                //函数体的添加
                methodBuilder.addStatement("atlas.put(\$S,\$T.build(\$T." + routeMeta.type + ", \$T.class,\$S,\$S))",
                        routeMeta.path,
                        ClassName.get(RouteMeta::class.java),
                        ClassName.get(RouteType::class.java),
                        ClassName.get(routeMeta.element as TypeElement?),
                        routeMeta.path,
                        routeMeta.group)
            }

            //生成AlterRouter_Group_xx类
            val groupClassName = RouteConstants.NAME_OF_GROUP + groupName + "_\$\$" + mProcessor.mModuleName!!.replace("-", "_")
            val typeSpec = TypeSpec.classBuilder(groupClassName)
                    .addSuperinterface(ClassName.get(iRouteGroup))
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(methodBuilder.build())
                    .build()
            val builder = JavaFile.builder(RouteConstants.PACKAGE_OF_GENERATE_FILE, typeSpec)
            val javaFile = builder.build()
            try {
                //写入文件
                javaFile.writeTo(mProcessor.mFiler)
                mProcessor.i("Generated RouteGroup：" + RouteConstants.PACKAGE_OF_GENERATE_FILE + "." + groupClassName)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            mRootMap[groupName] = groupClassName
        }
    }

    /**
     * 生成Root类  作用：记录<分组，对应的Group类>
     */
    private fun genRoot(iRouteRoot: TypeElement, iRouteGroup: TypeElement) {

        if (mRootMap.isEmpty()) {
            //没有group文件生成，则不需要生成root文件
            return
        }

        //创建参数类型 Map<String, List<Class<? extends IRouteGroup>>> routes>
        //Wildcard 通配符
        val iRouteGroupTypeName = ParameterizedTypeName.get(
                ClassName.get(Class::class.java),
                WildcardTypeName.subtypeOf(ClassName.get(iRouteGroup))
        )
        val parameterizedTypeName = ParameterizedTypeName.get(
                ClassName.get(MutableList::class.java),
                iRouteGroupTypeName)
        val mapTypeName = ParameterizedTypeName.get(
                ClassName.get(MutableMap::class.java),
                ClassName.get(String::class.java),
                parameterizedTypeName
        )

        //参数 Map<String,Class<? extends IRouteGroup>> routes> routes
        val parameter = ParameterSpec.builder(mapTypeName, "routes").build()
        val methodBuilder = MethodSpec.methodBuilder(RouteConstants.METHOD_LOAD_INTO)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override::class.java)
                .addParameter(parameter)

        //函数体
        mRootMap.entries.forEach { entry ->
            val className = ClassName.get(RouteConstants.PACKAGE_OF_GENERATE_FILE, entry.value)
            methodBuilder.beginControlFlow("if(routes.containsKey(\$S))", entry.key)
            methodBuilder.addStatement("List list = routes.get(\$S)", entry.key)
            methodBuilder.addStatement("list.add(\$T.class)", className)
            methodBuilder.addStatement("routes.put(\$S, list)", entry.key)
            methodBuilder.nextControlFlow("else")
            methodBuilder.addStatement("List<\$T> list = new \$T()", iRouteGroupTypeName, ArrayList::class.java)
            methodBuilder.addStatement("list.add(\$T.class)", className)
            methodBuilder.addStatement("routes.put(\$S, list)", entry.key)
            methodBuilder.endControlFlow()
        }

        //生成AlterRouter_Root_xx类
        val className = RouteConstants.NAME_OF_ROOT + "\$\$" + mProcessor.mModuleName!!.replace("-", "_")
        val typeSpec = TypeSpec.classBuilder(className)
                .addSuperinterface(ClassName.get(iRouteRoot))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodBuilder.build())
                .build()
        val builder = JavaFile.builder(RouteConstants.PACKAGE_OF_GENERATE_FILE, typeSpec)
        val build = builder.build()
        try {
            build.writeTo(mProcessor.mFiler)
            mProcessor.i("Generated RouteRoot：" + RouteConstants.PACKAGE_OF_GENERATE_FILE + "." + className)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}