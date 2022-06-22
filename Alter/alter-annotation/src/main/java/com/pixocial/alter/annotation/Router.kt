package com.pixocial.alter.annotation

/**
 * Alter 框架中，用于路由的注解
 *
 */

/**
 * 该注解用于路由设置跳转路径
 *
 * @param path
 * @param group
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Router(val path: String, val group: String = "")
