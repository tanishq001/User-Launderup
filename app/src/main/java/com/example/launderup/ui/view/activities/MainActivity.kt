package com.example.launderup.ui.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.launderup.R
import com.example.launderup.ui.view.fragments.CartFragment
import com.example.launderup.ui.view.fragments.HomeFragment
import com.example.launderup.ui.view.fragments.OrderFragment
import com.example.launderup.ui.view.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigation:BottomNavigationView
    private lateinit var expressButton:FloatingActionButton
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        expressButton=findViewById(R.id.floating_action_button)
        bottomNavigation=findViewById(R.id.bottomNavigation)
        bottomNavigation.background=null

        val intent:Intent=intent
        val cart:Boolean=intent.getBooleanExtra("Cart",false)
        val profile:Boolean=intent.getBooleanExtra("Profile",false)

        expressButton.setOnClickListener {
            val expressModalBottomSheet = ExpressModalBottomSheet()
            expressModalBottomSheet.show(supportFragmentManager, ExpressModalBottomSheet.TAG)
        }

        if(cart)
            replaceFragment(CartFragment())
        else if(profile)
            replaceFragment(ProfileFragment())
        else
        replaceFragment(HomeFragment())

        bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.page_1->replaceFragment(HomeFragment())
                R.id.page_2->replaceFragment(OrderFragment())
                R.id.page_4->replaceFragment(CartFragment())
                R.id.page_5->replaceFragment(ProfileFragment())
                
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.main_activity,fragment).commit()
    }
}