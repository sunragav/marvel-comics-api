package com.sunragav.indiecampers.marvelcomics

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.sunragav.indiecampers.feature_home.ui.views.ComicsListFeatureActivity


class SplashActivity : AppCompatActivity() {

    val mHandler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mHandler.postDelayed({
            val i = Intent(applicationContext, ComicsListFeatureActivity::class.java)
            startActivity(i)
        }, 500)
    }
}
