# Alter 接入文档  

`Alter` 是一个Android组件化框架，主要包含了以下几块功能：  
1. 支持自动生成 `module` 的 `module-api` 工程  
2. 支持服务的注册  
3. 支持路由跳转  
4. 支持 `module` 中注册 `application` 的生命周期相关回调  

## 一、自动生成 module 的 module-api 工程  

> 涉及工程：alter-gradle-plugin : 自动生成对应的 `module-api`。  
  生成的 `module-api` 代表的是该 `module` 需要对外提供的服务，所以`module-api` 中应该只包含对外的服务接口。

使用步骤： 
### 1. 配置工程下的 `build.gradle` 文件  

```groovy
apply from: "config.gradle"

buildscript {

    ext {
        kotlin_version = '1.3.72'
    }
    
    repositories {
        //参考demo本地repo方式
        jcenter()
        google()
    }

    dependencies {
        classpath 'com.android.tools.build:gradle:3.5.2'
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version" 

        classpath 'com.pixocial.alter:alter-gradle-plugin:1.0.0' //请查看最新的版本
    }
}
```
 
### 2. 配置 `setting.gradle` 文件  

```groovy
buildscript {
    repositories {
        //参考demo本地repo
        jcenter()
        google()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.5.2'
        classpath 'com.pixocial.alter:alter-gradle-plugin:1.0.0' //保持和`build.gradle` 文件中的一致
    }
}

Map<String, String> map = new HashMap<>(startParameter.projectProperties)
//设置需要生成 module-api 的 module工程
map.put("alterIncludeApiModules", ['module-a'])
startParameter.setProjectProperties(map)

apply plugin: 'alter-init' //应用alter-init，生成对应的-api模块
```
### 3. 配置 `ext`  

因为自动生成的 `module-api` 工程的 `build.config` 文件需要用到一些配置信息，所以需要配置一些公共参数。  
生成的 `build.config` 文件如下：  
```groovy
plugins {
    id 'com.android.library'
    id 'kotlin-android'
}

android {
    compileSdkVersion rootProject.ext.android.compileSdkVersion
    buildToolsVersion rootProject.ext.android.buildToolsVersion

    defaultConfig {
        minSdkVersion rootProject.ext.android.minSdkVersion
        targetSdkVersion rootProject.ext.android.targetSdkVersion
    }
}

dependencies {
    implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
}
```  

所以，需要配置用到的 `compileSdkVersion`、`buildToolsVersion` 等参数信息。（`kotlin_version` 在步骤1中已配置）  

```groovy
ext {

    android = [
            compileSdkVersion: 29,
            buildToolsVersion: "28.0.3",

            minSdkVersion    : 23,
            targetSdkVersion : 29,
    ]
}
```

### 4. 应用插件  

在 `build.gradle` 文件中应用 `alter-api` 插件  
```groovy
apply plugin: 'alter-api' //应用 alter-api 插件，配置源码目录及依赖关系
```

在`setting.gradle` 文件中应用 `alter-init` 插件  
```groovy
apply plugin: 'alter-init' //应用alter-init，生成对应的-api模块
```

### 5. 同步工程，生成对应的-api工程  

点击同步工程后，会在根目录下自动生成 `alter-build-api` 代码目录，`module-api` 都生成在此目录下  
  
## 二、服务的注册与使用  

> 服务的注册与使用需要使用到 `module-api` 工程，所以需要确保上一步已经完成  
>涉及工程：alter-gradle-plugin : 自动生成对应的 `module-api`  
>        alter-annotation ： 相关注解信息  
>        alter-core ：核心调用代码   

请先添加 `alter-annotation` 和 `alter-core` 依赖。  
```groovy
//需要提供服务的 module 中需要依赖 alter-annotation
implementation "com.pixocial.alter:alter-annotation:$rootProject.ext.component.alterAnnotation"
//需要使用服务的 module 中需要依赖 alter-core
implementation "com.pixocial.alter:alter-core:$rootProject.ext.component.alterCore"
```

### 1. 配置相关依赖  

`alter` 中提供了四种依赖关系，分别是： 
`alterApi`、`alterApiCompile`、`alterApiImplementation`、`alterCompileOnly`  

相关的使用如下：  
a. `alterApi` : 依赖对应 `module` 的 `module-api` 工程   
如果 `module-b` 需要用到 `module-a` 提供的服务接口，则在 `module-b` 中添加如下依赖：  
```groovy
alterApi project(":module-a") //在module-b中添加此行，表示module-b会依赖module-a生成的api工程module-a-api
```

b. `alterApiCompile` ：当前 `module` 对应生成的 `module-api` 需要依赖的库  
如果 `module-a` 生成的 `module-a-api` 工程需要有些依赖库，可以通过 `alterApiCompile` 配置，如：  
```groovy
alterApiCompile "xxx:xxx:1.0.0"
```
  
c. `alterApiImplementation` ： 同`alterApiCompile`  
因为新版本的 `gradle` 中已弃用 `compile`，所以可以使用 `alterApiImplementation`。  

d. `alterCompileOnly` ： 编译阶段当前 `module` 对应的 `module-api` 需要依赖的库  
类似 `alterApiCompile`，只是此依赖只在编译阶段有用。  

### 2. 新建 `module-api` 代码目录，并声明相关服务接口  
 
如果 `module-b` 需要用到 `module-a` 提供的服务接口，则在 `module-a` 中添加 `module-a-api` 代码目录。  
新建代码目录，需要在 `module-a` 根目录下，新建 `alter/java` 目录，然后在`alter/java` 目录添加提供的对外接口。  
如：  
![`module-api` 代码目录](./Alter/sample-img/module-a-api源码目录.png "`module-api` 代码目录")  
 
声明的服务接口如下：  
```kotlin
interface IModuleATest {
    fun test(): String
}
```

声明完接口后，需要在 `module-a` 实现具体的服务。  
如：  
```kotlin
@ServiceRegister(serviceInterface = IModuleATest::class)
class ModuleATest : IModuleATest {
    override fun test(): String {
        return "module a api"
    }
}
```  
服务实现类需要添加 `@ServiceRegister` 注解，并给 `serviceInterface` 赋值接口的类型

### 3. 使用服务  

调用 `AlterService.getService(接口类型)` 获取对应的服务。

在上一步中声明的 `IModuleATest` 服务，我们可以直接在 `module-b` 中使用，如：  
```kotlin
val service = AlterService.getService(IModuleATest::class.java) //获取服务，不缓存实例
service?.let {
    mTvMsg.text = it.test() //使用获取到的服务
}

//或者
val service = AlterService.getService(IModuleATest::class.java, true) //获取服务，缓存实例，下次获取此实例不会创建新实例
AlterService.destroyService(IModuleATest::class.java) //缓存实例后，在实例不再使用时，调用此接口，移除服务
```  

### 4. 接口服务的初始化和释放回调  

接口实现类中，可以实现 `IAlterServiceLifecycle` 接口，用于监听实现类的创建和释放。  

如：  
```kotlin
@ServiceRegister(serviceInterface = IModuleATest::class)
class ModuleATest : IModuleATest, IAlterServiceLifecycle {
    
    private val TAG = "ModuleATest"
    
    override fun test(): String {
        return "module a api"
    }

    override fun init() { //创建对象时回调
        Log.d(TAG, "ModuleATest init")
    }

    //此函数只有调用缓存实例的方式（AlterService.getService(IModuleATest::class.java, true)），
    //在调用AlterService.destroyService(IModuleATest::class.java)后，才会回调
    override fun unInit() { //释放对象时回调
        Log.d(TAG, "ModuleATest unInit")
    }
}
```  

## 三、路由跳转  
路由部分主要用于模块间的`activity`唤起。  

>涉及工程：alter-gradle-plugin : 路由表的自动注册    
>        alter-annotation ： 相关注解信息  
>        alter-core ：核心调用代码    
>        alter-compiler ：注解处理器  

### 1. `build.gradle` 配置  

`app` 工程的 `build.gradle` 中，添加：  
```groovy
apply plugin: 'alter-core'
```
   
使用到路由的模块（包括`app`）中，添加以下依赖：  
```groovy
apply plugin: 'kotlin-kapt'

android {
    defaultConfig {
        ...

        javaCompileOptions {
            annotationProcessorOptions {
                arguments = [moduleName: project.getName()]
            }
        }
    }
}

dependencies {
    implementation "com.pixocial.alter:alter-annotation:$rootProject.ext.component.alterAnnotation"
    implementation "com.pixocial.alter:alter-core:$rootProject.ext.component.alterCore"
    kapt "com.pixocial.alter:alter-compiler:$rootProject.ext.component.alterCompile"
}
```  

### 2. 为 `activity` 配置路由  

通过 `@Router` 注解，配置相关路由。  
如：  
```kotlin
@Router(path = "/module_b/main")
class ModuleBMainActivity : AppCompatActivity() {
}
```  

`@Router(path = "/module_b/main")` 中，`module_b` 代表的是组名，`main` 是唯一路径标识。请务必配置至少两级路径（包含组名）。  

***组名的作用是用于路由表的分组加载，避免一次性加载全部路由信息，加快加载速度，所以建议组名可以根据模块来定，或者是一个大功能块。***  

### 3. 路由调用  

通过 `AlterRouter` 构建路由调用。相关参数设置，可以参考 `Postcard.class` 类。    
```kotlin
AlterRouter.instance.build("/module_b/main").navigation(this)
```

## 四、模块中注册 `application` 的生命周期  

>涉及工程：alter-gradle-plugin : 代码的自动注册    
>        alter-annotation ： 相关注解信息  
>        alter-core ：核心调用代码    
>        alter-compiler ：注解处理器 

### 1. `build.gradle` 和 第三点路由跳转的一样，请参考路由跳转配置。  

### 2. 在子模块中，实现 `IAlterLifeCycle` 接口，并添加 `@AlterLifeCycle` 注解   

覆写相关方法，这些方法对应 `application` 中的方法。  

具体初始化单元，可以使用注解 `AlterLifeCycle`。  

如：  
```kotlin
@AlterLifeCycle
class ModuleAApplication : IAlterLifeCycle {
    override fun attachBaseContext(base: Context) {
        TODO("Not yet implemented")
    }

    override fun getPriority(): Int {
        return IAlterLifeCycle.PRIORITY_NORMAL
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        TODO("Not yet implemented")
    }

    override fun onCreate(context: Context) {
        TODO("Not yet implemented")
    }

    override fun onLowMemory() {
        TODO("Not yet implemented")
    }

    override fun onTrimMemory(level: Int) {
        TODO("Not yet implemented")
    }
}
```  

```kotlin
@AlterLifeCycle(process = LifeCycleProcess.MAIN, priority = 6, description = "ModuleAFirstInit")
class ModuleAFirstInit : IAlterLifeCycle {
    override fun attachBaseContext(base: Context?) {
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
    }

    override fun onCreate(context: Context) {
    }

    override fun onDependenciesCompleted(initName: String) {
    }

    override fun onLowMemory() {
    }

    override fun onTrimMemory(level: Int) {
    }
}
```

### 3. `app` 的 `application` 中，调用相应的周期函数  

在 `application` 中，调用 `AlterLifeCycleManager` 对应的周期函数。  

如：  
```kotlin
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Alter.init(this)

        AlterLifeCycleManager.instance.onCreate(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        AlterLifeCycleManager.instance.attachBaseContext(base)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        AlterLifeCycleManager.instance.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        AlterLifeCycleManager.instance.onTrimMemory(level)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        AlterLifeCycleManager.instance.onConfigurationChanged(newConfig)
    }
}
```

## 五、Alter 初始化  

设计路由表信息的注册，建议在 `Application` 的 `onCreate` 中初始化。

```kotlin
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Alter.init(this)
    }
}  
```  

## 六、 混淆  

```groovy
#alter
-keep class com.pixocial.alter.core.** {*;}
-keep class **.*$$AlterLifeCycleBinder{*;}
-keep class **.*$$AlterBinder{*;}
-keep class * implements com.pixocial.alter.core.service.IAlterServiceProvider {*;}
-keep class * implements com.pixocial.alter.core.lifecycle.IAlterLifeCycle {*;}
-keep interface com.pixocial.**.api.**{*;}
```  

以及暴露的 `api` 的接口，需要不被混淆。  
建议统一用一个规则的包名，可以统一配置，如：  
```groovy
-keep interface com.pixocial.**.api.**{*;}
```  




  
  
