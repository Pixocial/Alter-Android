package com.pixocial.alter.core.router.utils

import android.util.Log
import com.pixocial.alter.core.router.IAlterRouteGroup
import com.pixocial.alter.core.router.IAlterRouteRoot
import com.pixocial.alter.model.RouteMeta
import java.util.*

/**
 * router 数据中心，用于存放路由的映射信息
 */
internal class AlterRouteCenter private constructor() {

    /**
     * root 映射表 保存分组信息
     */
    val groupsIndex: MutableMap<String, MutableList<Class<out IAlterRouteGroup?>>> = HashMap()

    /**
     * group 映射表 保存组中的所有数据
     */
    val routes: MutableMap<String, RouteMeta> = HashMap()

    companion object {
        @JvmStatic
        val instance = SingletonHolder.sHolder
    }

    private object SingletonHolder {
        val sHolder = AlterRouteCenter()
    }

    fun init() {
        loadRouteInfo()
        Log.d("AlterRouteCenter", "groupsIndex ${groupsIndex.size}")
    }

    /**
     * 插件自动填充，不用手动添加
     */
    private fun loadRouteInfo() {
    }

    private fun registerRoute(className: String) {
        Log.d("AlterRouteCenter", "AlterRouteCenter $className")
        val rootPath = RouteConstants.PACKAGE_OF_GENERATE_FILE + "." + RouteConstants.NAME_OF_ROOT
        if (className.startsWith(rootPath)) {
            try {
                //拿到反射字节码
                val aClass = Class.forName(className)
                //获取对象
                val o = aClass.getConstructor().newInstance()
                (o as IAlterRouteRoot).loadInto(groupsIndex)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}