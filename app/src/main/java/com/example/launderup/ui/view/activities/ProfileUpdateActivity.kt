package com.example.launderup.ui.view.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.launderup.R
import com.example.launderup.data.api.HerokuInstance
import com.example.launderup.data.models.UserDetailsUpdate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileUpdateActivity : AppCompatActivity() {
    private lateinit var nameTextField: EditText
    private lateinit var phoneTextField: EditText
    private lateinit var emailTextField: EditText
    private lateinit var cityTextField: EditText
    private lateinit var pinCodeTextField: EditText
    private lateinit var updateBtn: Button
    private val sharedPrefFile = "LaunderUp"
    private lateinit var sharedPreferences: SharedPreferences
    private var confirmDetails:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_update)

        nameTextField=findViewById(R.id.profile_update_name1)
        phoneTextField=findViewById(R.id.profile_update_mobile_number1)
        emailTextField=findViewById(R.id.profile_update_email1)
        cityTextField=findViewById(R.id.profile_update_city1)
        pinCodeTextField=findViewById(R.id.profile_update_pin_code1)
        updateBtn=findViewById(R.id.update_btn)
        sharedPreferences=this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        val localName: String? =sharedPreferences.getString("username","")
        val localEmail: String? =sharedPreferences.getString("email","")
        val localCity: String? =sharedPreferences.getString("city","")
        val localPinCode: String? =sharedPreferences.getString("pinCode","")
        val localMobileNumber: String? =sharedPreferences.getString("mobileNumber","")
        val localUID:String?=sharedPreferences.getString("UID","ABC")
        val intent:Intent=intent
        confirmDetails=intent.getBooleanExtra("ConfirmDetails",false)

        phoneTextField.isEnabled=false
        cityTextField.isEnabled=false
        nameTextField.setText(localName)
        phoneTextField.setText(localMobileNumber.toString())
        emailTextField.setText(localEmail)
        cityTextField.setText(localCity)
        pinCodeTextField.setText(localPinCode)

        val backBtn: ImageView =findViewById(R.id.profile_update_back_button)
        backBtn.setOnClickListener {
        }

        updateBtn.setOnClickListener {
            if (localUID != null) {
                val name=nameTextField.text
                val mobileNumber=phoneTextField.text
                val email=emailTextField.text
                val city=cityTextField.text
                val pinCode=pinCodeTextField.text
                userDetailsUpdate(name.toString(),mobileNumber.toString(),email.toString(),city.toString(),pinCode.toString(),localUID)
            }
        }
    }

    private fun userDetailsUpdate(name:String,phone:String,email:String,city:String,pinCode:String,uid:String) {
        val getOTP= HerokuInstance.herokuapi.userDetailsUpdate(uid=uid,phoneNumber = phone,name=name,email=email,city=city,pinCode=pinCode)
        getOTP.enqueue(object : Callback<UserDetailsUpdate> {
            override fun onResponse(call: Call<UserDetailsUpdate>, response: Response<UserDetailsUpdate>) {
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString("username", name)
                    .putString("email",email)
                    .putString("city",city)
                    .putString("pinCode",pinCode)
                    .putString("mobileNumber",phone)
                editor.apply()
                editor.commit()
                if(response.body()!!.result=="Details Updated") {
                    if(confirmDetails) {
                        startActivity(Intent(applicationContext,ConfirmDetailsActivity::class.java))
                        finish()
                    }
                    else{
                        startActivity(Intent(applicationContext,MainActivity::class.java).putExtra("Profile",true))
                        finish()
                    }
                    Toast.makeText(applicationContext,"Details Updated Successfully", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<UserDetailsUpdate>, t: Throwable) {
                Log.i(MainActivity::class.simpleName, "Details Update Failed")
            }
        })

    }
}