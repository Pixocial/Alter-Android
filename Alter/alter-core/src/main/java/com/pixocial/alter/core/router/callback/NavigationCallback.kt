package com.pixocial.alter.core.router.callback

import com.pixocial.alter.core.router.Postcard

/**
 * 路由跳用回调处理
 */
interface NavigationCallback {

    /**
     * 找到跳转页面
     * @param postcard
     */
    fun onFound(postcard: Postcard)

    /**
     * 未找到
     * @param postcard
     */
    fun onLost(postcard: Postcard)

    /**
     * 成功跳转
     * @param postcard
     */
    fun onArrival(postcard: Postcard)

}