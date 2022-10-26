package com.example.launderup.ui.view.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.launderup.R
import com.example.launderup.data.api.HerokuInstance.Companion.herokuapi
import com.example.launderup.data.api.RetrofitInstance.Companion.api
import com.example.launderup.data.models.ResendOTP
import com.example.launderup.data.models.UserLogin
import com.example.launderup.data.models.VerifyOTP
import com.goodiebag.pinview.Pinview
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VerifyOTPActivity : AppCompatActivity() {
    private lateinit var verifyCodeButton:Button
    private lateinit var resendOTPButton:TextView
    private lateinit var pinView: Pinview

    private val sharedPrefFile = "LaunderUp"
    private lateinit var sharedPreferences:SharedPreferences

    private lateinit var phoneNumber:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_otpactivity)

        //Binding different views with activity
        verifyCodeButton=findViewById(R.id.verify_code_button)
        resendOTPButton=findViewById(R.id.resend_otp)
        pinView=findViewById(R.id.pin_view)

        sharedPreferences=this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        val intent:Intent= intent
        val mobileNumber=intent.getLongExtra("mobile_number",0)
        phoneNumber=mobileNumber.toString().replace("91","")
        
        verifyCodeButton.setOnClickListener {
            val otp=pinView.value
            verify(otp.toString().toInt(),mobileNumber)
        }

        resendOTPButton.setOnClickListener {
            resend(mobileNumber)
        }
    }

    private fun verify(otp:Int, mobileNumber:Long){
        val verify=api.verifyOTP(otp = otp, mobileNumber = mobileNumber)
        verify.enqueue(object : Callback<VerifyOTP> {
            override fun onResponse(call: Call<VerifyOTP>, response: Response<VerifyOTP>) {
                if(response.body()!!.type == "success")
                    userLogin()
                else
                    Toast.makeText(applicationContext, "Enter Correct OTP", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call<VerifyOTP>, t: Throwable) {
                Log.i(MainActivity::class.simpleName, "OTP Verification Failed")
            }
        })
    }

    private fun resend(mobileNumber:Long){
        val resend=api.resendOTP(mobileNumber = mobileNumber)
        resend.enqueue(object : Callback<ResendOTP> {
            override fun onResponse(call: Call<ResendOTP>, response: Response<ResendOTP>) {
                Toast.makeText(applicationContext,"OTP Resend Successfully",Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call<ResendOTP>, t: Throwable) {
                Log.i(MainActivity::class.simpleName, "OTP Resend Failed")
            }
        })
    }

    private fun userLogin() {
        val userLogin= herokuapi.userLogin(mobileNumber = phoneNumber)
        userLogin.enqueue(object : Callback<UserLogin> {
            override fun onResponse(call: Call<UserLogin>, response: Response<UserLogin>) {
                if(response.code()==200) {
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString("UID", response.body()!!.uid)
                        .putString("mobileNumber", phoneNumber)
                        .putString("ClothesArray", null)
                        .putString("clothOrderID", null)
                    editor.apply()
                    editor.commit()
                    if (response.body()!!.account_status == "Created" || response.body()!!.account_status == "Created but phone number exists")
                        startActivity(Intent(applicationContext, RegisterActivity::class.java))
                    else if (response.body()!!.account_status == "Logged In")
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                }
            }
            override fun onFailure(call: Call<UserLogin>, t: Throwable) {
                Log.i(MainActivity::class.simpleName, "User Login Failed")
            }
        })
    }
}