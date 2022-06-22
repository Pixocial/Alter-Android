package com.pixocial.alter.demo

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.pixocial.alter.core.router.AlterRouter
import com.pixocial.alter.demo.R
import com.pixocial.alter.module_base.Person
import com.pixocial.alter.module_base.Constants
import com.pixocial.alter.module_base.hashMap
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun clickEnterA(view: View) {
        AlterRouter.instance.build(Constants.MODULE_A_MAIN, Constants.GROUP_1).navigation(this)
    }

    fun clickEnterB(view: View) {
        AlterRouter.instance.build(Constants.MODULE_B_MAIN, Constants.GROUP_2).navigation(this)
    }

    fun clickEnterAWithArgs(view: View) {
        AlterRouter.instance.build(Constants.MODULE_A_MAIN, Constants.GROUP_1)
            .withInt("withInt", hashMap["withInt"] as Int).navigation(this)
    }

    fun clickEnterPage1(view: View) {
        AlterRouter.instance.build(Constants.MAIN_PAGE1, Constants.GROUP_1).navigation(this)
    }

    fun clickEnterPage2(view: View) {
        AlterRouter.instance.build(Constants.MAIN_PAGE2, Constants.GROUP_1).navigation(this)
    }

    @Suppress("UNCHECKED_CAST")
    fun clickEnterPage1WithArgs(view: View) {
        hashMap["withOptionsCompat"] = ActivityOptionsCompat.makeCustomAnimation(
            applicationContext,
            R.anim.enter_anim,
            R.anim.exit_anim
        )
        val person = Person("name", 18)
        val person1 = Person("name_1", 19)
        val person2 = Person("name_2", 20)
        hashMap["withParcelable"] = person as Parcelable
        hashMap["withParcelableArray"] = arrayOf(person, person1, person2) as Array<Parcelable?>
        hashMap["withParcelableArrayList"] =
            arrayListOf(person, person1, person2) as ArrayList<Parcelable?>

        val alterRouter = AlterRouter.instance.build(Constants.MAIN_PAGE1, Constants.GROUP_1)
        alterRouter.withBooleanArray(
            "withBooleanArray",
            hashMap["withBooleanArray"] as BooleanArray
        )
        alterRouter.withBoolean("withBoolean", hashMap["withBoolean"] as Boolean)
        alterRouter.withBundle(hashMap["withBundle"] as Bundle)
        alterRouter.withByte("withByte", hashMap["withByte"] as Byte)
        alterRouter.withByteArray("withByteArray", hashMap["withByteArray"] as ByteArray)
        alterRouter.withChar("withChar", hashMap["withChar"] as Char)
        alterRouter.withCharArray("withCharArray", hashMap["withCharArray"] as CharArray)
        alterRouter.withDouble("withDouble", hashMap["withDouble"] as Double)
        alterRouter.withDoubleArray("withDoubleArray", hashMap["withDoubleArray"] as DoubleArray)
        alterRouter.withFlags(hashMap["withFlags"] as Int)
        alterRouter.withFloat("withFloat", hashMap["withFloat"] as Float)
        alterRouter.withFloatArray("withFloatArray", hashMap["withFloatArray"] as FloatArray)
        alterRouter.withInt("withInt", hashMap["withInt"] as Int)
        alterRouter.withIntArray("withIntArray", hashMap["withIntArray"] as IntArray)
        alterRouter.withIntegerArrayList(
            "withIntegerArrayList",
            hashMap["withIntegerArrayList"] as ArrayList<Int>
        )
        alterRouter.withLong("withLong", hashMap["withLong"] as Long)
        alterRouter.withLongArray("withLongArray", hashMap["withLongArray"] as LongArray)
        alterRouter.withOptionsCompat(hashMap["withOptionsCompat"] as ActivityOptionsCompat)
        alterRouter.withParcelable("withParcelable", hashMap["withParcelable"] as Parcelable)
        alterRouter.withParcelableArray(
            "withParcelableArray",
            hashMap["withParcelableArray"] as Array<Parcelable?>
        )
        alterRouter.withParcelableArrayList(
            "withParcelableArrayList",
            hashMap["withParcelableArrayList"] as ArrayList<out Parcelable?>
        )
        alterRouter.withShort("withShort", hashMap["withShort"] as Short)
        alterRouter.withShortArray("withShortArray", hashMap["withShortArray"] as ShortArray)
        alterRouter.withString("withString", hashMap["withString"] as String?)
        alterRouter.withStringArray("withStringArray", hashMap["withStringArray"] as Array<String>)
        alterRouter.withStringArrayList(
            "withStringArrayList",
            hashMap["withStringArrayList"] as ArrayList<String>
        )
        alterRouter.withTransition(R.anim.enter_anim, R.anim.exit_anim)
        alterRouter.navigation(this)
    }

    fun clickJavaPage(view: View) {
        AlterRouter.instance.build(Constants.MAIN_PAGE_JAVA, Constants.GROUP_1).navigation(this)
    }

    fun clickKotlinPage(view: View) {
        AlterRouter.instance.build(Constants.MAIN_PAGE_KOTLIN, Constants.GROUP_1).navigation(this)
    }

    fun clickForResultPage(view: View) {
        AlterRouter.instance.build(Constants.MAIN_PAGE_FOR_RESULT, Constants.GROUP_1)
            .navigation(this)
    }

    fun clickFragment(view: View) {
        AlterRouter.instance.build(Constants.MAIN_PAGE_FRAGMENT, Constants.GROUP_1).navigation(this)
    }

    fun clickAnimation(view: View) {
        AlterRouter.instance.build(Constants.MAIN_PAGE_ANIMATION, Constants.GROUP_1)
            .withTransition(R.anim.enter_anim, R.anim.exit_anim)
            .withOptionsCompat(
                ActivityOptionsCompat.makeCustomAnimation(
                    applicationContext,
                    R.anim.enter_anim,
                    R.anim.exit_anim
                )
            )
            .navigation(this)
    }

    fun getFragment(view: View) {
        val result = AlterRouter.instance.build(Constants.BLACK_FRAGMENT)
                .withString("key1", "value1")
                .navigation(this)
        Log.d("MainActivity", "getFragment $result")
    }
}