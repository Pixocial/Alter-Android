package com.pixocial.alter.annotation

import kotlin.reflect.KClass

/**
 *
 * Alter 框架中接口实现类的注册注解
 *
 * @param serviceInterface 进行注册的interface类
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ServiceRegister(val serviceInterface: KClass<*>)