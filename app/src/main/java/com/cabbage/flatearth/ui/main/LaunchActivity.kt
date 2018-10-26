package com.cabbage.flatearth.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.OnClick
import com.cabbage.flatearth.R

class LaunchActivity : AppCompatActivity() {

    @OnClick(R.id.btn_start)
    fun start() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        ButterKnife.bind(this)
    }
}