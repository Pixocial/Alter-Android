package com.pixocial.alter.module_a

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.pixocial.alter.annotation.Router
import com.pixocial.alter.module_a.R
import com.pixocial.alter.module_base.Constants
import com.pixocial.alter.module_base.hashMap
import java.lang.StringBuilder

@Router(path = Constants.MODULE_A_MAIN, group = Constants.GROUP_1)
class ModuleAMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module_a_main)
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
                sb.append("key: $item , value: $value\n")
            } else {
                if (arrayOf(value).contentDeepEquals(arrayOf(hashMap[item]))) {
                    sb.append("key: $item , value: ${arrayOf(value)}\n")
                } else {
                    println("Exception----$item")
                }
            }
        }
    }

    fun back(view: View) {
        finish()
    }
}