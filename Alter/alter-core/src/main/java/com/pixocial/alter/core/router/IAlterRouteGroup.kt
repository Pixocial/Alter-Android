package com.pixocial.alter.core.router

import com.pixocial.alter.model.RouteMeta

interface IAlterRouteGroup {
    fun loadInto(atlas: Map<String, RouteMeta>)
}