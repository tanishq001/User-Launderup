package com.example.launderup.ui.view.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.launderup.R
import com.example.launderup.data.api.HerokuInstance
import com.example.launderup.data.models.NewOrder
import com.example.launderup.data.models.VerifyOrder
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentMethodActivity : AppCompatActivity(),PaymentResultWithDataListener {
    private val TAG = PaymentMethodActivity::class.java.simpleName
    private lateinit var paymentContinueButton: Button

    private val sharedPrefFile = "LaunderUp"
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var razorpayPaymentID:String
    private lateinit var razorpayOrderID:String
    private lateinit var razorpaySignature:String
    private lateinit var email:String
    private lateinit var mobile:String
    private lateinit var clothOrderID:String
    private lateinit var paymentID:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_method)

        Checkout.preload(this)
        sharedPreferences=this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val userID=sharedPreferences.getString("UID","").toString()
        val shopID=sharedPreferences.getString("shopID","").toString()
        email=sharedPreferences.getString("email","").toString()
        mobile=sharedPreferences.getString("mobileNumber","").toString()

        val intent:Intent=intent

        val pickupDate:String=intent.getStringExtra("pickupDate").toString()
        val deliveryDate="16:30,20:06:2022"
        val geolocation="http//sdvjksnk"
        val address=intent.getStringExtra("address").toString()
        val express=intent.getStringExtra("express").toString()
        val serviceType=intent.getStringExtra("serviceType").toString()
        val totalCost=intent.getStringExtra("totalOrderCost").toString()
        val clothesTypes=intent.getStringExtra("clothesTypes").toString().trim()
        val clothes=JSONArray(clothesTypes)
        val clothees=clothes[0] as JSONObject
        val clothesType:JSONObject= JSONObject()
        clothesType.put("mens",clothes)

        paymentContinueButton=findViewById(R.id.continue_payment_button)
        paymentContinueButton.setOnClickListener {
            Log.i("Payment Method Activity Details","$userID $shopID $pickupDate $deliveryDate $geolocation $address $express $serviceType $totalCost $clothesType")
            placeOrder(userID,shopID,pickupDate,deliveryDate,geolocation,address, express,serviceType,totalCost,clothesType)
        }

        val paymentGroup: RadioGroup =findViewById(R.id.paymentRadioGroup)
        val checkedRadioButtonId = paymentGroup.checkedRadioButtonId
        paymentGroup.setOnCheckedChangeListener{group,checkId->
            when(checkId){
                R.id.paytm_radio_button->{
                    Toast.makeText(applicationContext, "Paytm Selected", Toast.LENGTH_SHORT).show()
                }
                R.id.razorpay_radio_button->{
                    Toast.makeText(applicationContext, "Razorpay Selected", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startPayment(paymentOrderID:String) {
        val activity: Activity = this
        val co = Checkout()
        co.setKeyID("rzp_test_fsINoU7sl53QSj")

        try {
            val options = JSONObject()
            options.put("name","LaunderUp")
            options.put("description","Order Charges")
            options.put("theme.color", "#3399cc");
            options.put("currency","INR");
            options.put("order_id", paymentOrderID)
            options.put("prefill.email", email);
            options.put("prefill.contact",mobile);

            val retryObj =  JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            co.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }

    }
    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        if (p1 != null) {
            razorpayPaymentID=p1.paymentId
            razorpayOrderID=p1.orderId
            razorpaySignature=p1.signature
//            verifyOrder(clothOrderID,paymentID)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("clothOrderID",clothOrderID)
            editor.apply()
            editor.commit()
            startActivity(Intent(applicationContext, OrderPlacedActivity::class.java))
        }
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Toast.makeText(this,p1,Toast.LENGTH_SHORT).show()
    }

    private fun placeOrder(userID:String,shopID:String,pickupDate:String,deliveryDate:String,geolocation:String,address:String, express:String,serviceType:String,totalCost:String,clothesTypes:JSONObject){
        val placeOrder= HerokuInstance.herokuapi.userNewOrder(
            uid =userID, shopID = shopID, pickUpDate = pickupDate, deliveryDate =deliveryDate,
            geolocation =geolocation, userAddress = address, serviceType =serviceType, expressAvailable =express,
            totalCost = totalCost, clothesTypes = clothesTypes)
        placeOrder.enqueue(object : Callback<NewOrder> {
            override fun onResponse(call: Call<NewOrder>, response: Response<NewOrder>) {
                if(response.code()==200) {
                    clothOrderID=response.body()!!.cloth_order_id
                    paymentID=response.body()!!.payment_id
                    startPayment(response.body()!!.payment_order_id)
                }

            }
            override fun onFailure(call: Call<NewOrder>, t: Throwable) {
                Log.i("Failed", call.toString(), t)
            }
        })
    }

    private fun verifyOrder(clothOrderID:String,paymentID:String){
        val verifyOrder= HerokuInstance.herokuapi.verifyOrder(clothOrderID = clothOrderID, paymentID = paymentID,
            razorpayPaymentID = razorpayPaymentID, razorpayOrderID = razorpayOrderID, razorpaySignature = razorpaySignature)
        verifyOrder.enqueue(object : Callback<VerifyOrder> {
            override fun onResponse(call: Call<VerifyOrder>, response: Response<VerifyOrder>) {
                if(response.body()!!.status=="Order Placed" && response.code()==200)
                {
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString("clothOrderID",clothOrderID)
                    editor.apply()
                    editor.commit()
                    startActivity(Intent(applicationContext, OrderPlacedActivity::class.java))
                }

            }
            override fun onFailure(call: Call<VerifyOrder>, t: Throwable) {
                Log.i(MainActivity::class.simpleName, "Order Verification Failed")
            }
        })
    }


}