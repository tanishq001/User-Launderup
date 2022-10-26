package com.example.launderup.ui.view.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.launderup.R
import com.example.launderup.data.api.HerokuInstance
import com.example.launderup.data.models.UserDetails
import com.example.launderup.ui.view.activities.LoginActivity
import com.example.launderup.ui.view.activities.MainActivity
import com.example.launderup.ui.view.activities.OrderHistoryActivity
import com.example.launderup.ui.view.activities.ProfileUpdateActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : Fragment() {
    private lateinit var logoutButton: Button
    private lateinit var editProfileButton:Button
    private lateinit var orderHistoryButton:Button

    private lateinit var profileName:TextView
    private lateinit var profilePhone:TextView
    private lateinit var profileEmail:TextView
    private lateinit var profileAddress:TextView

    private lateinit var name:String
    private lateinit var phone:String
    private lateinit var email:String
    private lateinit var city:String
    private lateinit var pinCode:String


    private val sharedPrefFile = "LaunderUp"
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root= inflater.inflate(R.layout.fragment_profile, container, false)
        logoutButton=root.findViewById(R.id.logout_btn)
        editProfileButton=root.findViewById(R.id.edit_profile_btn)
        orderHistoryButton=root.findViewById(R.id.order_history_btn)
        profileName=root.findViewById(R.id.profile_name)
        profilePhone=root.findViewById(R.id.profile_phone_number)
        profileEmail=root.findViewById(R.id.profile_email)
        profileAddress=root.findViewById(R.id.profile_address)



        sharedPreferences=context!!.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val userID: String =sharedPreferences.getString("UID","").toString()
        name=sharedPreferences.getString("username","").toString()
        phone=sharedPreferences.getString("mobileNumber","").toString()
        email=sharedPreferences.getString("email","").toString()
        city=sharedPreferences.getString("city","").toString()
        pinCode=sharedPreferences.getString("pinCode","").toString()
        if(name=="")
        getUserDetails(userID)
        profileName.text=name
        profilePhone.text=phone
        profileEmail.text=email
        profileAddress.text="$city, $pinCode"


        logoutButton.setOnClickListener {
            MaterialAlertDialogBuilder(context!!)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout"){dialog, which->
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build()
                    val mGoogleSignInClient = GoogleSignIn.getClient(context!!, gso)
                    mGoogleSignInClient.signOut()
                    sharedPreferences.edit().clear().commit()
                    startActivity(Intent(context,LoginActivity::class.java))
                }
                .setNegativeButton("Cancel"){dialog, which->
                    dialog.dismiss()
                }

                .show()

        }

        editProfileButton.setOnClickListener {
            val intent = Intent(context, ProfileUpdateActivity::class.java)
            startActivity(intent)
        }

        orderHistoryButton.setOnClickListener {
            startActivity(Intent(context,OrderHistoryActivity::class.java))
        }

        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun getUserDetails(UID:String) {
        val getUserDetails= HerokuInstance.herokuapi.getUserDetails(userID=UID)
        getUserDetails.enqueue(object : Callback<UserDetails> {
            override fun onResponse(call: Call<UserDetails>, response: Response<UserDetails>) {
                if(response.code()==200) {
                    name=response.body()!!.name
                    phone=response.body()!!.phone
                    email=response.body()!!.email
                    city=response.body()!!.city
                    pinCode=response.body()!!.pin
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString("username", name)
                        .putString("email",email)
                        .putString("city", city)
                        .putString("pinCode",pinCode)
                    editor.apply()
                    editor.commit()

                }
            }
            override fun onFailure(call: Call<UserDetails>, t: Throwable) {
                Log.i(MainActivity::class.simpleName, "User Registration Failed")
            }
        })
    }
}