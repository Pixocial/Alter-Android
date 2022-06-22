package com.pixocial.alter.core.service

/**
 * clazz的辅助类用来生成实际的classImpl类
 *
 */
interface IAlterServiceProvider<T> {

    /**
     * 根据clazz创建实例对象并注入到Alter中
     */
    fun buildAlterService(clazz: Class<T>): T

}