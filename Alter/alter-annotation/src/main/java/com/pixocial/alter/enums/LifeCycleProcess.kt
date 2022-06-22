package com.pixocial.alter.enums

/**
 * 在哪个进程初始化
 */
enum class LifeCycleProcess {
    // 主进程
    MAIN,

    // 所有进程
    ALL,

    // 非主进程
    OTHER
}