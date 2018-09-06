package com.wawi.android.wawidemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.xiaopan.java.util.RegexUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RegexUtils.isEmail("");
    }

}
