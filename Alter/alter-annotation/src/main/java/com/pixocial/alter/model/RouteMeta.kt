package com.pixocial.alter.model

import com.pixocial.alter.annotation.Router
import com.pixocial.alter.enums.RouteType
import javax.lang.model.element.Element

class RouteMeta {

    companion object {
        @JvmStatic
        fun build(type: RouteType?, destination: Class<*>?, path: String, group: String): RouteMeta {
            return RouteMeta(type, destination, group, path, null)
        }
    }

    var element: Element? = null
    var destination: Class<*>? = null
    var path = ""
    var group = ""
    var type: RouteType? = null

    constructor()

    constructor(type: RouteType?, router: Router, element: Element?): this(type, null, router, element)

    constructor(type: RouteType?, destination: Class<*>?, router: Router, element: Element?): this(type, destination, router.group, router.path, element)

    constructor(type: RouteType?, destination: Class<*>?, group: String, path: String, element: Element?) {
        this.type = type
        this.element = element
        this.destination = destination
        this.path = path
        this.group = group
    }


}