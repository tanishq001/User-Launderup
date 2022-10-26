package com.example.launderup.ui.view.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.launderup.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NeedHelpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_need_help)

        val backBtn:ImageView=findViewById(R.id.need_help_back_button)
        val mailButton:FloatingActionButton =findViewById(R.id.mail_button)

        mailButton.setOnClickListener {
            val intent=Intent().apply {
                action=Intent.ACTION_SEND
                type="text/plain"
                putExtra(Intent.EXTRA_EMAIL,"launderup@launderup.com")
            }
            intent.setPackage("com.google.android.gm")
            startActivity(intent)
        }

        backBtn.setOnClickListener {
            val intent=Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}