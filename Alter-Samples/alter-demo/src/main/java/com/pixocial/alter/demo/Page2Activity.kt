package com.pixocial.alter.demo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pixocial.alter.annotation.Router
import com.pixocial.alter.demo.R
import com.pixocial.alter.module_base.Constants

@Router(path = Constants.MAIN_PAGE2, group = Constants.GROUP_1)
class Page2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_2)
    }

    fun back(view: View) {
        val intent = Intent()
        intent.putExtra("result", "success")
        setResult(RESULT_OK, intent)
        finish()
    }
}