package com.pixocial.alter.core.router

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.core.app.ActivityOptionsCompat
import com.pixocial.alter.core.router.callback.NavigationCallback
import com.pixocial.alter.enums.RouteType
import java.util.*

/**
 * 路由跳转信息类
 */
class Postcard {

    var path: String? = null
    var group: String? = null
    var bundle: Bundle = Bundle()
    var flags = -1
    //新版风格
    var optionsCompat: Bundle? = null
    //转场动画
    var enterAnim = 0
    var exitAnim = 0
    var destination: Class<*>? = null
    var action: String? = null
    var data: Uri? = null
    var type: RouteType? = null

    constructor(path: String, group: String) : this(path, group, null)

    constructor(path: String, group: String, bundle: Bundle?) {
        this.path = path
        this.group = group
        this.bundle = bundle ?: Bundle()
    }

    fun getExtras(): Bundle? {
        return bundle
    }

    /**
     * 设置flags
     * @param flags              flags
     * @return                  Postcard
     */
    fun withFlags(flags: Int): Postcard {
        this.flags = flags
        return this
    }

    /**
     * 跳转动画
     * @param enterAnim         进入动画
     * @param exitAnim          退出动画
     * @return                  Postcard
     */
    fun withTransition(enterAnim: Int, exitAnim: Int): Postcard {
        this.enterAnim = enterAnim
        this.exitAnim = exitAnim
        return this
    }

    /**
     * 转场动画
     * @param compat            compat
     * @return                  Postcard
     */
    fun withOptionsCompat(compat: ActivityOptionsCompat): Postcard {
        optionsCompat = compat.toBundle()
        return this
    }

    fun withBundle(bundle: Bundle): Postcard {
        this.bundle = bundle
        return this
    }

    fun withString(key: String, value: String?): Postcard {
        bundle.putString(key, value)
        return this
    }

    fun withBoolean(key: String, value: Boolean): Postcard {
        bundle.putBoolean(key, value)
        return this
    }


    fun withShort(key: String, value: Short): Postcard {
        bundle.putShort(key, value)
        return this
    }


    fun withInt(key: String, value: Int): Postcard {
        bundle.putInt(key, value)
        return this
    }


    fun withLong(key: String, value: Long): Postcard {
        bundle.putLong(key, value)
        return this
    }


    fun withDouble(key: String, value: Double): Postcard {
        bundle.putDouble(key, value)
        return this
    }


    fun withByte(key: String, value: Byte): Postcard {
        bundle.putByte(key, value)
        return this
    }


    fun withChar(key: String, value: Char): Postcard {
        bundle.putChar(key, value)
        return this
    }


    fun withFloat(key: String, value: Float): Postcard {
        bundle.putFloat(key, value)
        return this
    }


    fun withParcelable(key: String, value: Parcelable): Postcard {
        bundle.putParcelable(key, value)
        return this
    }


    fun withStringArray(key: String, value: Array<String>): Postcard {
        bundle.putStringArray(key, value)
        return this
    }


    fun withBooleanArray(key: String, value: BooleanArray): Postcard {
        bundle.putBooleanArray(key, value)
        return this
    }


    fun withShortArray(key: String, value: ShortArray): Postcard {
        bundle.putShortArray(key, value)
        return this
    }


    fun withIntArray(key: String, value: IntArray): Postcard {
        bundle.putIntArray(key, value)
        return this
    }


    fun withLongArray(key: String, value: LongArray): Postcard {
        bundle.putLongArray(key, value)
        return this
    }


    fun withDoubleArray(key: String, value: DoubleArray): Postcard {
        bundle.putDoubleArray(key, value)
        return this
    }


    fun withByteArray(key: String, value: ByteArray): Postcard {
        bundle.putByteArray(key, value)
        return this
    }


    fun withCharArray(key: String, value: CharArray): Postcard {
        bundle.putCharArray(key, value)
        return this
    }


    fun withFloatArray(key: String, value: FloatArray): Postcard {
        bundle.putFloatArray(key, value)
        return this
    }


    fun withParcelableArray(key: String, value: Array<Parcelable?>): Postcard {
        bundle.putParcelableArray(key, value)
        return this
    }

    fun withParcelableArrayList(key: String, value: ArrayList<out Parcelable?>): Postcard {
        bundle.putParcelableArrayList(key, value)
        return this
    }

    fun withIntegerArrayList(key: String, value: ArrayList<Int>): Postcard {
        bundle.putIntegerArrayList(key, value)
        return this
    }

    fun withStringArrayList(key: String, value: ArrayList<String>): Postcard {
        bundle.putStringArrayList(key, value)
        return this
    }

    fun withAction(action: String?): Postcard {
        this.action = action
        return this
    }

    fun withData(uri: Uri?): Postcard {
        this.data = uri
        return this
    }

    fun navigation(): Any? {
        return AlterRouter.instance.navigation(null, this, -1, null)
    }

    fun navigation(context: Context?): Any? {
        return AlterRouter.instance.navigation(context, this, -1, null)
    }


    fun navigation(context: Context?, callback: NavigationCallback?): Any? {
        return AlterRouter.instance.navigation(context, this, -1, callback)
    }

    fun navigation(context: Context?, requestCode: Int): Any? {
        return AlterRouter.instance.navigation(context, this, requestCode, null)
    }

    fun navigation(context: Context?, requestCode: Int, callback: NavigationCallback?): Any? {
        return AlterRouter.instance.navigation(context, this, requestCode, callback)
    }
}