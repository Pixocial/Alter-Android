package com.pixocial.alter.core.router

interface IAlterRouteRoot {
    fun loadInto(routes: MutableMap<String, MutableList<Class<out IAlterRouteGroup?>>>)
}