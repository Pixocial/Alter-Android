package com.pixocial.alter.core.service


/**
 * Alter Service 对外类，用于获取 Alter 接口服务
 *
 */
class AlterService {

    companion object {

        /**
         * 根据serviceInterface获取实际的impl实例
         *
         * @param serviceInterface 接口类型
         */
        @JvmStatic
        fun <T> getService(serviceInterface: Class<T>): T? {
            return getService(serviceInterface, false)
        }

        /**
         * 根据serviceInterface获取实际的impl实例
         *
         * @param serviceInterface 接口类型
         * @param isCache 是否读取缓存。
         *                true: 先从缓存中拿数据，如果没有，则新建一个实例，并加入缓存；加入缓存的，请调用destroyService方法释放。
         *                false: 每次都新建一个实例，且不加入缓存。
         */
        @JvmStatic
        fun <T> getService(serviceInterface: Class<T>, isCache: Boolean): T? {
            return AlterServiceCenter.getAlterService(serviceInterface, isCache)
        }

        /**
         * 销毁serviceInterface对应的impl实例，并调用unInit(假如是AltersLifecycle)。
         * 注意：只有调用getService(serviceInterface: Class<T>, isCache: Boolean)的，才需要调用此方法，才会有unInit的回调
         *
         * @param serviceInterface 接口类型
         */
        @JvmStatic
        fun <T> destroyService(serviceInterface: Class<T>) {
            AlterServiceCenter.removeAlterService(serviceInterface)
        }
    }

}