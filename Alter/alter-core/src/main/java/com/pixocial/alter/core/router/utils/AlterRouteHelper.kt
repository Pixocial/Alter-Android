package com.pixocial.alter.core.router.utils

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import androidx.core.app.ActivityCompat
import com.pixocial.alter.core.Alter
import com.pixocial.alter.core.router.Postcard
import com.pixocial.alter.core.router.callback.NavigationCallback
import com.pixocial.alter.core.router.exception.NoRouteFoundException

/**
 * 准备跳转相关信息
 */
internal fun prepareCard(card: Postcard) {
    val path = card.path
    val group = card.group
    var routeMeta = AlterRouteCenter.instance.routes[path]
    if (routeMeta == null) {
        val groupMetaList = AlterRouteCenter.instance.groupsIndex[card.group]
                ?: throw NoRouteFoundException("can't found router in mapping. group=${group}, path=${path}")
        if (groupMetaList.size <= 0) {
            throw NoRouteFoundException("can't found router in mapping. group=${group}, path=${path}")
        }
        groupMetaList.forEach { groupMeta ->
            val iGroupInstance = try {
                groupMeta.getConstructor().newInstance()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            } ?: throw RuntimeException("fail to get the group route mapping.")
            iGroupInstance.loadInto(AlterRouteCenter.instance.routes)
        }
        AlterRouteCenter.instance.groupsIndex.remove(group)
        routeMeta = AlterRouteCenter.instance.routes[path]
    }
    if (routeMeta == null) {
        throw NoRouteFoundException("can't found router in mapping. group=${group}, path=${path}")
    }
    card.destination = routeMeta.destination
    card.type = routeMeta.type
}

/**
 * 获得组别
 */
internal fun extractGroup(path: String): String? {
    if (TextUtils.isEmpty(path) || !path.startsWith("/")) {
        throw RuntimeException("$path, don't have group.")
    }
    return try {
        val defaultGroup = path.substring(1, path.indexOf("/", 1))
        if (TextUtils.isEmpty(defaultGroup)) {
            throw RuntimeException("$path, don't have group.")
        } else {
            defaultGroup
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * 跳转到activity
 */
fun startToActivity(
        context: Context?, handler: Handler, postcard: Postcard,
        requestCode: Int, callback: NavigationCallback?
) {
    val currentContext = context ?: Alter.context
    val intent = Intent(currentContext, postcard.destination)
    intent.putExtras(postcard.bundle)
    val flags: Int = postcard.flags
    if (-1 != flags) {
        intent.flags = flags
    } else if (currentContext !is Activity) {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    if (postcard.action != null) {
        intent.action = postcard.action
    }
    if (postcard.data != null) {
        intent.data = postcard.data
    }
    if (Looper.myLooper() == Looper.getMainLooper()) {
        startActivity(currentContext, intent, postcard, requestCode, callback)
    } else {
        handler.post {
            startActivity(currentContext, intent, postcard, requestCode, callback)
        }
    }
}

private fun startActivity(
        currentContext: Context, intent: Intent, postcard: Postcard,
        requestCode: Int, callback: NavigationCallback?
) {
    val bundle: Bundle? = postcard.optionsCompat
    //可能需要返回码
    if (requestCode > 0 && currentContext is Activity) {
        ActivityCompat.startActivityForResult(currentContext,
                intent, requestCode, bundle)
    } else {
        ActivityCompat.startActivity(currentContext, intent, bundle)
    }
    if (currentContext is Activity) {
        if (0 != postcard.enterAnim || 0 != postcard.exitAnim) {
            //老版本
            currentContext.overridePendingTransition(
                    postcard.enterAnim, postcard.exitAnim)
        }
    }
    //跳转完成
    callback?.onArrival(postcard)
}

fun getFragment(postcard: Postcard): Any? {
    val fragmentMeta: Class<*>? = postcard.destination
    try {
        val newInstance = fragmentMeta?.getConstructor()?.newInstance()
        newInstance?.let {
            if (it is Fragment) {
                it.arguments = postcard.getExtras()
            }
        }
        return newInstance
    } catch (ex: java.lang.Exception) {
        ex.printStackTrace()
    }
    return null
}