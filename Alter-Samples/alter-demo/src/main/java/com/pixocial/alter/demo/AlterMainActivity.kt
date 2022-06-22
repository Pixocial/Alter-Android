package com.pixocial.alter.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import com.pixocial.alter.core.router.AlterRouter
import com.pixocial.alter.demo.R
import com.pixocial.alter.module_base.Person
import com.pixocial.alter.module_base.Constants
import com.pixocial.alter.module_base.hashMap
import java.util.ArrayList

class AlterMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alter_main)
    }

    @Suppress("UNCHECKED_CAST")
    fun toModuleB(view: View) {

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

        val alterRouter = AlterRouter.instance.build(Constants.MODULE_B_MAIN, Constants.GROUP_2)
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
}