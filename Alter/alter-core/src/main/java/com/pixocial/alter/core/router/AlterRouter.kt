package com.pixocial.alter.core.router

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import com.pixocial.alter.core.router.callback.NavigationCallback
import com.pixocial.alter.core.router.exception.NoRouteFoundException
import com.pixocial.alter.core.router.utils.*
import com.pixocial.alter.enums.RouteType

/**
 * 路由入口类
 */
class AlterRouter private constructor() {

    private val mHandler: Handler = Handler(Looper.getMainLooper())

    companion object {
        @JvmStatic
        val instance = SingletonHolder.sHolder

        @JvmStatic
        fun init() {
            Log.d("AlterRouter", "AlterRouter init")
            AlterRouteCenter.instance.init()
        }
    }

    private object SingletonHolder {
        val sHolder = AlterRouter()
    }

    /**
     * 传入path路径
     * 比如：path="/app/main"
     * 那么path="/app/main" group="app"
     * @param path                      path路径
     * @return                          Postcard信息类
     */
    fun build(path: String): Postcard {
        return build(path, extractGroup(path))
    }

    /**
     * 传入path路径
     * 比如：path="/app/main"
     * 那么path="/app/main" group="app"
     * @param path                      path路径
     * @param group                     group组
     * @return                          Postcard信息类
     */
     fun build(path: String?, group: String?): Postcard {
        return if (TextUtils.isEmpty(path) || TextUtils.isEmpty(group)) {
            throw RuntimeException("router path is error!!")
        } else {
            Postcard(path!!, group!!)
        }
    }

    /**
     * 最终会调用这个方法
     * @param context                   上下文
     * @param postcard                  Postcard
     * @param requestCode               请求码
     * @param callback                  回调callback
     * @return
     */
    fun navigation(context: Context?, postcard: Postcard, requestCode: Int,
                   callback: NavigationCallback?): Any? {
        try {
            prepareCard(postcard)
        } catch (e: NoRouteFoundException) {
            e.printStackTrace()
            //没找到，可能是路径配置错误
            callback?.onLost(postcard)
            return null
        }
        //找到对应的路径呢
        callback?.onFound(postcard)
        when (postcard.type) {
            RouteType.ACTIVITY -> {
                startToActivity(context, mHandler, postcard, requestCode, callback)
            }
            RouteType.FRAGMENT -> {
                return getFragment(postcard)
            }
            else -> {
                Log.w("AlterRouter", "navigation route type no found!!")
                return null
            }
        }
        return null
    }

}