package com.pixocial.alter.core.service

import android.util.Log
import java.util.concurrent.ConcurrentHashMap

/**
 * Alter 实例管理类
 */
class AlterServiceCenter {

    companion object {

        private const val TAG = "AlterCenter"

        /**
         * 存储实例的map
         */
        private val mCenter: ConcurrentHashMap<Class<*>, Any?> = ConcurrentHashMap()

        /**
         * 根据clazz获取实际的实例
         */
        @Suppress("UNCHECKED_CAST")
        fun <T> getAlterService(clazz: Class<T>, isCache: Boolean): T? {
            var alter: T? = null
            if (isCache) {
                alter = mCenter[clazz] as T?
            }
            if (alter == null) {
                synchronized(clazz) {
                    try {
                        if (isCache) {
                            alter = mCenter[clazz] as T?
                        }
                        if (alter == null) {
                            val clazzName = "${clazz.canonicalName}\$\$AlterBinder"
                            val alterProvider = Class.forName(clazzName).newInstance()
                            alter = (alterProvider as? IAlterServiceProvider<T>)?.buildAlterService(clazz)
                            (alter as? IAlterServiceLifecycle)?.init()
                            if (isCache) {
                                mCenter[clazz] = alter
                            }
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "newInstance Alter Service fail, $e")
                        return null
                    }
                }
            }
            return alter as T
        }

        /**
         * 销毁serviceInterface对应的impl实例，并调用unInit(IAlterServiceLifecycle)
         */
        fun <T> removeAlterService(clazz: Class<T>) {
            (mCenter.remove(clazz) as? IAlterServiceLifecycle)?.unInit()
        }
    }

}