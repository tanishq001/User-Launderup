package com.example.launderup.ui.view.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.launderup.R
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory

class AboutUsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        val backBtn: ImageView = findViewById(R.id.about_us_back_button)
        val privacyPolicy: TextView = findViewById(R.id.privacy_policy_textview)
        val rateUs: TextView = findViewById(R.id.rate_us_textview)
        val tnc: TextView = findViewById(R.id.tnc_textview)

        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        privacyPolicy.setOnClickListener {
            val openURL = Intent(android.content.Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://www.launderup.com/privacypolicy.html")
            startActivity(openURL)
        }

        tnc.setOnClickListener {
            val openURL = Intent(android.content.Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://www.launderup.com/terms.html")
            startActivity(openURL)
        }

        rateUs.setOnClickListener {
            val manager: ReviewManager = ReviewManagerFactory.create(this)
            val request = manager.requestReviewFlow()
            request.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val reviewInfo:ReviewInfo = task.result
                    val flow = manager.launchReviewFlow(this, reviewInfo)
                    flow.addOnCompleteListener { _ ->
                    }
                } else {
//                    @ReviewErrorCode val reviewErrorCode = (task.getException() as TaskException).errorCode
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }
//            val intent = Intent(Intent.ACTION_VIEW).apply {
//                data =
//                    Uri.parse("http://play.google.com/store/search?q=swordigo&c=apps")
//            }
//            startActivity(intent)


        }
    }
}