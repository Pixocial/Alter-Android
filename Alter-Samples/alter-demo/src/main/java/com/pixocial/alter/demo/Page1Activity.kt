package com.pixocial.alter.demo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.pixocial.alter.annotation.Router
import com.pixocial.alter.core.router.AlterRouter
import com.pixocial.alter.core.router.Postcard
import com.pixocial.alter.core.router.callback.NavigationCallback
import com.pixocial.alter.demo.R
import com.pixocial.alter.module_base.Constants
import com.pixocial.alter.module_base.hashMap
import java.lang.StringBuilder

@Router(path = Constants.MAIN_PAGE1, group = Constants.GROUP_1)
class Page1Activity : AppCompatActivity() {
    val TAG = "Page1Activity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_1)
        val sb = StringBuilder()
        getData(sb)
        if (sb.isNotEmpty()) {
            findViewById<TextView>(R.id.title).text = "从主页传递过来的参数："
            findViewById<TextView>(R.id.contents).text = sb.toString()
        }
    }

    private fun getData(sb: StringBuilder) {
        val extras = intent.extras
        for (item in hashMap.keys) {
            val value = extras?.get(item) ?: continue
            if (value == hashMap[item]) {
                println("success")
                sb.append("key: $item , value: $value\n")
            } else {
                if (arrayOf(value).contentDeepEquals(arrayOf(hashMap[item]))) {
                    println("success--")
                    sb.append("key: $item , value: ${arrayOf(value)}\n")
                } else {
                    println("Exception----$item")
                }
            }
        }
    }

    fun enterPage2(view: View) {
        AlterRouter.instance.build(Constants.MAIN_PAGE2, Constants.GROUP_1)
            .navigation(this, object : NavigationCallback {
                override fun onArrival(postcard: Postcard) {
                    Log.d(TAG, "onArrival: ")
                }

                override fun onFound(postcard: Postcard) {
                    Log.d(TAG, "onFound: ")
                }

                override fun onLost(postcard: Postcard) {
                    Log.d(TAG, "onLost: ")
                }

            })
    }

    fun back(view: View) {
        finish()
    }

}