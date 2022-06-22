package com.pixocial.alter.demo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pixocial.alter.annotation.Router
import com.pixocial.alter.demo.R
import com.pixocial.alter.module_base.Constants

@Router(path = Constants.MAIN_PAGE_KOTLIN, group = Constants.GROUP_1)
class KotlinPageActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_kotlin)
    }

    fun back(view: View) {
        finish()
    }
}