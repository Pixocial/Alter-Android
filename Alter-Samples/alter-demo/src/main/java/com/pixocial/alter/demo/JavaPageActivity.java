package com.pixocial.alter.demo;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pixocial.alter.annotation.Router;
import com.pixocial.alter.demo.R;
import com.pixocial.alter.module_base.Constants;

@Router(path = Constants.MAIN_PAGE_JAVA, group = Constants.GROUP_1)
public class JavaPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_java);
    }

    public void back(View view) {
        finish();
    }
}
