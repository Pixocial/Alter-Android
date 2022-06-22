package com.pixocial.alter.core.router.exception

import java.lang.RuntimeException

/**
 * 找不到对应路由的异常
 */
class NoRouteFoundException(detailsMsg: String) : RuntimeException(detailsMsg)