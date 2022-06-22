package com.pixocial.alter.module_b

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.pixocial.alter.annotation.Router
import com.pixocial.alter.core.service.AlterService
import com.pixocial.alter.module_a.api.IModuleATest
import com.pixocial.alter.module_b.R
import com.pixocial.alter.module_base.Constants
import com.pixocial.alter.module_base.hashMap

@Router(path = Constants.MODULE_B_MAIN, group = Constants.GROUP_2)
class ModuleBMainActivity : AppCompatActivity() {

    lateinit var mTvMsg: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module_b_main)
        mTvMsg = findViewById(R.id.tv_module_b)
        val extras = intent.extras
        for (item in hashMap.keys) {
            val value = extras?.get(item) ?: continue
            if (value == hashMap[item]) {
                println("success")
            } else {
                if (arrayOf(value).contentDeepEquals(arrayOf(hashMap[item]))) {
                    println("success--")
                } else {
                    println("error----$item")
                }
            }
        }

    }

    fun getModuleAMsg(view: View) {
        val service = AlterService.getService(IModuleATest::class.java, true)
        Log.d("ModuleBMainActivity", "service:" + service)
        service?.let {
            mTvMsg.text = it.test()
        }
    }

    fun back(view: View) {
        finish()
    }
}