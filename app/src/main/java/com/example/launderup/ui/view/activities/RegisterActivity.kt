package com.example.launderup.ui.view.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.launderup.R
import com.example.launderup.data.api.HerokuInstance.Companion.herokuapi
import com.example.launderup.data.models.UserRegister
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var nameTextField:EditText
    private lateinit var phoneTextField:EditText
    private lateinit var emailTextField:EditText
    private lateinit var addressTextField:EditText
    private lateinit var cityTextField:EditText
    private lateinit var pinCodeTextField:EditText
    private lateinit var registerButton:Button

    private val sharedPrefFile = "LaunderUp"
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //binding views with activity
        nameTextField=findViewById(R.id.name_et)
        phoneTextField=findViewById(R.id.mobile_number_et)
        emailTextField=findViewById(R.id.email_et)
        addressTextField=findViewById(R.id.address_et)
        cityTextField=findViewById(R.id.city_et)
        pinCodeTextField=findViewById(R.id.pin_code_et)
        registerButton=findViewById(R.id.register_button)

        sharedPreferences=this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        val name=nameTextField.text
        val email=emailTextField.text
        val address=addressTextField.text
        val pinCode=pinCodeTextField.text
        val mobileNumber: String =sharedPreferences.getString("mobileNumber","").toString()
        phoneTextField.setText(mobileNumber)
        cityTextField.setText("New Delhi")

        registerButton.setOnClickListener {
            if(name.isEmpty())
                Toast.makeText(this,"Enter Name",Toast.LENGTH_SHORT).show()
            else if(email.isEmpty())
                Toast.makeText(this,"Enter Email",Toast.LENGTH_SHORT).show()
            else if(address.isEmpty())
                Toast.makeText(this,"Enter Address",Toast.LENGTH_SHORT).show()
            else if(pinCode.isEmpty())
                Toast.makeText(this,"Enter PinCode",Toast.LENGTH_SHORT).show()
            else if(pinCode.length!=6 && pinCode.substring(0,2)!="11")
                Toast.makeText(this,"Enter Valid PinCode",Toast.LENGTH_SHORT).show()
            else {
                val userID= sharedPreferences.getString("UID",null)
                if (userID != null) {
                    userRegister(name.toString(), mobileNumber, email.toString(), pinCode.toString(),userID)
                }
            }
        }
    }

    private fun userRegister(name:String,phone:String,email:String,pinCode:String,userID:String) {
        val getOTP= herokuapi.userRegister(uid=userID,phoneNumber=phone,name=name,email=email,pinCode=pinCode)
        getOTP.enqueue(object : Callback<UserRegister> {
            override fun onResponse(call: Call<UserRegister>, response: Response<UserRegister>) {
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString("username", name)
                    .putString("email",email)
                    .putString("city","New Delhi")
                    .putString("pinCode",pinCode)
                    .putString("ClothesArray",null)
                    .putString("clothOrderID",null)
                editor.apply()
                editor.commit()
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
               Toast.makeText(applicationContext,"Welcome",Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call<UserRegister>, t: Throwable) {
                Log.i(MainActivity::class.simpleName, "User Registration Failed")
            }
        })
    }
}