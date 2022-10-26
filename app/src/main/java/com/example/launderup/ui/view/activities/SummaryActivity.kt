package com.example.launderup.ui.view.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.launderup.R

class SummaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        val summaryBtn:Button=findViewById(R.id.place_order_button)

        summaryBtn.setOnClickListener {
            val intent=Intent(this, OrderPlacedActivity::class.java)
            startActivity(intent)
        }
    }
}