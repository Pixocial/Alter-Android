package com.pixocial.alter.compiler.router

class RouteConstants {
    companion object {
        private const val SEPARATOR = "_"
        const val PROJECT = "AlterRouter"
        const val NAME_OF_GROUP = PROJECT + SEPARATOR + "Group" + SEPARATOR
        const val NAME_OF_ROOT = PROJECT + SEPARATOR + "Root" + SEPARATOR

        const val PACKAGE_OF_GENERATE_FILE = "com.pixocial.alter.core.router.routes"

        const val PACKAGE_OF_LIFECYCLE_FILE = "$PACKAGE_OF_GENERATE_FILE.lifecycle"

        const val ACTIVITY = "android.app.Activity"
        const val FRAGMENT = "android.app.Fragment"
        const val FRAGMENT_V4 = "android.support.v4.app.Fragment"

        const val I_ROUTE_GROUP = "com.pixocial.alter.core.router.IAlterRouteGroup"
        const val I_ROUTE_ROOT = "com.pixocial.alter.core.router.IAlterRouteRoot"
        const val METHOD_LOAD_INTO = "loadInto"

        const val ARGUMENTS_NAME = "moduleName"
    }
}