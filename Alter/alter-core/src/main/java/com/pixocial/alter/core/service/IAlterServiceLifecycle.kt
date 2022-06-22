package com.pixocial.alter.core.service

/**
 * 实例生命周期接口
 * 实现此接口，在实例化具体对象后，会回调init方法，移除实例时，回调unInit，可以根据此做一些初始化和释放的工作
 */
interface IAlterServiceLifecycle {
    /**
     * 初始化
     */
    fun init()

    /**
     * 析构释放
     * 此函数只有调用缓存实例的方式（AlterService.getService(xxx::class.java, true)），
     * 在调用AlterService.destroyService(xxx::class.java)后，才会回调
     */
    fun unInit()
}