package com.pixocial.alter.plugins.utils

class DependencyConstant {

    /**
     * alterApi configurationName
     * alterApi 依赖对应module的api
     *      例如: alterApi project(":lib_base")
     *      表示module会依赖lib_base-api module
     */
    public static final String ALTER_API = "alterApi"

    /**
     * alterApiCompile configurationName
     * alterApiCompile 当前module对应的api module需要依赖的库
     *      例如: alterApiCompile "xxx:xxx:1.0.0"
     *      表示这个module对应的api module也会依赖xxx这个库
     */
    public static final String ALTER_API_COMPILE = "alterApiCompile"

    /**
     * alterApiImplementation configurationName
     * alterApiImplementation 当前module对应的api module需要依赖的库
     *      例如: alterApiImplementation "xxx:xxx:1.0.0"
     *      表示这个module对应的api module也会依赖xxx这个库
     */
    public static final String ALTER_API_IMPLEMENTATION = "alterApiImplementation"

    /**
     * alterCompileOnly configurationName
     * alterCompileOnly 当前module对应的api module需要依赖的库
     *      例如: alterCompileOnly "xxx:xxx:1.0.0"
     *      表示这个module对应的api module也会依赖xxx这个库，但是对应的依赖库不会进行pom发布
     */
    public static final String ALTER_API_COMPILE_ONlY = "alterCompileOnly"

}