package com.example.launderup.ui.view.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.launderup.R

class SplashScreenActivity : AppCompatActivity() {
    private val sharedPrefFile = "LaunderUp"
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        sharedPreferences = this.getSharedPreferences(sharedPrefFile,Context.MODE_PRIVATE)
        Handler(Looper.getMainLooper()).postDelayed({
            if(sharedPreferences.contains("UID")) {
                if(sharedPreferences.contains("username"))
                    startActivity(Intent(this, MainActivity::class.java))
                else
                    startActivity(Intent(this, RegisterActivity::class.java))
            }
            else
                startActivity(Intent(this, LoginActivity::class.java))

            finish()
        }, 3000)
    }
}