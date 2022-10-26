package com.example.launderup.ui.view.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.launderup.R
import com.example.launderup.data.api.HerokuInstance
import com.example.launderup.data.models.OrderData
import com.example.launderup.data.models.UserOrders
import com.example.launderup.ui.adapter.OrderHistoryAdapter
import com.google.android.material.progressindicator.CircularProgressIndicator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderHistoryActivity : AppCompatActivity() {
    private lateinit var orderHistory:RecyclerView
    private lateinit var progressIndicator:CircularProgressIndicator
    private lateinit var sharedPreferences: SharedPreferences
    private val sharedPrefFile = "LaunderUp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)

        orderHistory=findViewById(R.id.order_history_rv)
        progressIndicator=findViewById(R.id.order_history_progress)
        orderHistory.layoutManager= LinearLayoutManager(this)
        sharedPreferences=this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val userID:String=sharedPreferences.getString("UID","").toString()
        getOrderHistory(userID)
    }

    private fun getOrderHistory(userID:String){
        val uid: String? =sharedPreferences.getString("UID"," ")
        val getOrderHistory= uid?.let { HerokuInstance.herokuapi.getUserOrders(uid=userID) }
        getOrderHistory?.enqueue(object : Callback<UserOrders> {
            override fun onResponse(call: Call<UserOrders>, response: Response<UserOrders>) {
                progressIndicator.hide()
                val data:List<OrderData> =response.body()!!.data
                val orderHistoryAdapter = OrderHistoryAdapter(this@OrderHistoryActivity,data)
                orderHistory.adapter=orderHistoryAdapter

                orderHistoryAdapter.setOnItemClickListener(object : OrderHistoryAdapter.OnItemClickListener{
                    override fun onItemClick(position: Int) {
                        Toast.makeText(applicationContext,"Order Clicked",Toast.LENGTH_SHORT).show()
                    }
                })
            }
            override fun onFailure(call: Call<UserOrders>, t: Throwable) {
                Log.i(MainActivity::class.simpleName, "Orders History Fetch Failed")
            }
        })
    }
}