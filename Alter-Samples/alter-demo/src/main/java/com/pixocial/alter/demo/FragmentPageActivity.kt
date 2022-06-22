package com.pixocial.alter.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pixocial.alter.annotation.Router
import com.pixocial.alter.demo.R
import com.pixocial.alter.module_base.Constants


@Router(path = Constants.MAIN_PAGE_FRAGMENT, group = Constants.GROUP_1)
class FragmentPageActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_fragment)
    }
}