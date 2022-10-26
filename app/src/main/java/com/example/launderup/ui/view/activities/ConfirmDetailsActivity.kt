package com.example.launderup.ui.view.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.launderup.R

class ConfirmDetailsActivity : AppCompatActivity() {
    private lateinit var  detailLayout:ConstraintLayout
    private lateinit var continueButton:Button
    private lateinit var editDetailsButton:Button

    private lateinit var confirmDetailsName: TextView
    private lateinit var confirmDetailsPhone:TextView
    private lateinit var confirmDetailsAddress:TextView

    private lateinit var name:String
    private lateinit var phone:String
    private lateinit var city:String
    private lateinit var pinCode:String


    private val sharedPrefFile = "LaunderUp"
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_details)

        detailLayout=findViewById(R.id.details_layout)
        confirmDetailsName=findViewById(R.id.details_confirm_name)
        confirmDetailsPhone=findViewById(R.id.details_confirm_mobile)
        confirmDetailsAddress=findViewById(R.id.details_confirm_address)

        continueButton=findViewById(R.id.continue_button)
        editDetailsButton=findViewById(R.id.change_address_button)

        sharedPreferences=this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        name=sharedPreferences.getString("username","").toString()
        phone=sharedPreferences.getString("mobileNumber","").toString()
        city=sharedPreferences.getString("city","").toString()
        pinCode=sharedPreferences.getString("pinCode","").toString()

        confirmDetailsName.text=name
        confirmDetailsPhone.text=phone
        confirmDetailsAddress.text="$city, $pinCode"

        continueButton.setOnClickListener {
            val prevIntent:Intent=intent
            val totalOrderCost:String?=prevIntent.getStringExtra("totalOrderCost")
            val pickupDate:String?=prevIntent.getStringExtra("pickupDate")
            val pickupTime:String?=prevIntent.getStringExtra("pickupTime")
            val clothesTypes:String?=prevIntent.getStringExtra("clothesTypes")
            val intent=Intent(this,PaymentMethodActivity::class.java)
                .putExtra("totalOrderCost",totalOrderCost)
                .putExtra("pickupDate",pickupDate)
                .putExtra("pickupTime",pickupTime)
                .putExtra("address","$city $pinCode")
                .putExtra("express","true")
                .putExtra("serviceType","ironing")
                .putExtra("clothesTypes",clothesTypes)
            Log.i("Confirm Details Activity",totalOrderCost.toString())

            startActivity(intent)

        }

        editDetailsButton.setOnClickListener {
            startActivity(Intent(this,ProfileUpdateActivity::class.java).putExtra("ConfirmDetails",true))
            finish()
        }
    }
}