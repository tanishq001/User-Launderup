package com.example.launderup.ui.view.activities

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.launderup.R

class EditAddressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_address)

        val saveAddressBtn:Button=findViewById(R.id.save_address_button)

        saveAddressBtn.setOnClickListener {
            finish()
        }
    }
}