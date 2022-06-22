package com.pixocial.alter.demo

import android.annotation.SuppressLint
import android.content.Intent
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

@Router(path = Constants.MAIN_PAGE_FOR_RESULT, group = Constants.GROUP_1)
class ForResultPageActivity : AppCompatActivity() {
    val TAG = "ForResultPageActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_for_result)

    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val extras = data?.extras
            findViewById<TextView>(R.id.contents).text = "返回值：${extras?.get("result")}"
        }
    }

    fun clickForResult(view: View) {
        AlterRouter.instance.build(Constants.MAIN_PAGE2, Constants.GROUP_1)
            .navigation(this, 1, object : NavigationCallback {
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