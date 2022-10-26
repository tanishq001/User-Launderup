package com.example.launderup.ui.view.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.launderup.R
import com.example.launderup.data.api.RetrofitInstance.Companion.api
import com.example.launderup.data.models.SendOTP
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


const val RC_SIGN_IN=123
const val ABC=10

class LoginActivity : AppCompatActivity(){
    private lateinit var personName:String
    private lateinit var personEmail:String
    private lateinit var loginButton:Button
    private lateinit var fbButton:Button
    private lateinit var googleButton:SignInButton
    private lateinit var numberTextField:EditText
    private lateinit var number:Editable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton=findViewById(R.id.login_button) //binding Login Button
        fbButton=findViewById(R.id.facebook_login_button) //Binding Facebook Button
        googleButton=findViewById(R.id.google_sign_in_button) //Binding Google Button
        numberTextField=findViewById(R.id.phone_num)  //Binding Phone Number Text Field

        numberTextField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                number=s
                if(number.toString().length==10)
                loginButton.isEnabled=true
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,before: Int, count: Int) {
                if(count<before || count>before)
                    loginButton.isEnabled=false
            }
        })

        //Login Button Functionality
        loginButton.setOnClickListener {
            if(number.isNotEmpty())
            {
                var mobileNumber:String=number.toString()
                mobileNumber= "91$mobileNumber"
                sendOtp(mobileNumber.toLong())
            }
            else
                Toast.makeText(this, "Enter Number", Toast.LENGTH_SHORT).show()

//            startActivity(Intent(this,RegisterActivity::class.java))
        }


//        facebook login starts
        LoginManager.getInstance().retrieveLoginStatus(this, object : LoginStatusCallback {
            override fun onCompleted(accessToken: AccessToken) {

            }

            override fun onFailure() {
                // No access token could be retrieved for the user
            }

            override fun onError(exception: Exception) {
                // An error occurred
            }
        })

        fbButton.setOnClickListener {
            val callbackManager = CallbackManager.Factory.create()
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult) {
                        startActivityForResult(intent, ABC)
                    }

                    override fun onCancel() {
                        Toast.makeText(applicationContext,"Login Unsuccessful",Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(error: FacebookException) {

                    }
                })
            val accessToken = AccessToken.getCurrentAccessToken()
            val isLoggedIn = accessToken != null && !accessToken.isExpired
//            LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile"))
        }
//        .....................................facebook code ends.................................


//        ......................................google code starts................................
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        googleButton.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
//        ......................................google code ends..................................


    }

//    override fun onStart() {
//        val account = GoogleSignIn.getLastSignedInAccount(this)
//        if(account!= JSONObject.NULL){
//            val intent=Intent(this,MainActivity::class.java)
//            startActivity(intent)
//        }
//        else{
//            Toast.makeText(applicationContext, "Not Signed In", Toast.LENGTH_SHORT).show()
//        }
//        super.onStart()
//    }

        @Deprecated("Deprecated in Java")
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callbackManager = CallbackManager.Factory.create()
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

            if (requestCode == RC_SIGN_IN) {
                val task: Task<GoogleSignInAccount> =GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            }

            else if(requestCode== ABC){
                finish()
                val acct = GoogleSignIn.getLastSignedInAccount(applicationContext)
                if (acct != null) {
                    personName = acct.displayName.toString()
                    val personGivenName = acct.givenName
                    personEmail = acct.email.toString()
                    val personPhoto: Uri? = acct.photoUrl
                }
                val intent=Intent(this, RegisterActivity::class.java)
                intent.putExtra("user_name",personName)
                intent.putExtra("user_email",personEmail)
                startActivity(intent)
            }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val acct = GoogleSignIn.getLastSignedInAccount(applicationContext)
            if (acct != null) {
                personName = acct.displayName.toString()
                val personGivenName = acct.givenName
                 personEmail = acct.email.toString()
                val personPhoto: Uri? = acct.photoUrl
            }
            val intent=Intent(this, RegisterActivity::class.java)
            intent.putExtra("user_name",personName)
            intent.putExtra("user_email",personEmail)
            startActivity(intent)
        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            Toast.makeText(applicationContext, "Sign_In Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendOtp(number: Long) {
        val getOTP=api.getOTP(mobileNumber = number)
        getOTP.enqueue(object : Callback<SendOTP> {
            override fun onResponse(call: Call<SendOTP>, response: Response<SendOTP>) {
                val intent=Intent(applicationContext, VerifyOTPActivity::class.java)
                intent.putExtra("mobile_number",number)
                startActivity(intent)
                finish()
            }
            override fun onFailure(call: Call<SendOTP>, t: Throwable) {
                Log.i(MainActivity::class.simpleName, "Login Failure")
            }
        })
    }
}