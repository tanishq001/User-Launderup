package com.example.launderup.ui.view.fragments

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.launderup.R
import com.example.launderup.data.api.HerokuInstance
import com.example.launderup.data.models.CancelOrder
import com.example.launderup.data.models.OrderData
import com.example.launderup.ui.view.activities.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class OrderFragment : Fragment() {
    private lateinit var orderPlacedStatusBox:ImageView
    private lateinit var orderPlacedStatusLine:ImageView
    private lateinit var orderPickedStatusBox:ImageView
    private lateinit var orderPickedStatusLine:ImageView
    private lateinit var orderProcessingStatusBox:ImageView
    private lateinit var orderProcessingStatusLine:ImageView
    private lateinit var orderCompletedStatusBox:ImageView
    private lateinit var orderCompletedStatusLine:ImageView
    private lateinit var orderDeliveredStatusBox:ImageView
    private lateinit var orderPlacedStatusTV:TextView
    private lateinit var orderPickedStatusTV:TextView
    private lateinit var orderProcessingStatusTV:TextView
    private lateinit var orderCompletedStatusTV:TextView
    private lateinit var orderDeliveredStatusTV:TextView
    private lateinit var noOrderPlacedTextView: TextView

    private lateinit var cancelOrderButton: Button
    private lateinit var orderStatusProgressIndicator: CircularProgressIndicator
    private lateinit var orderStatusLayout:ConstraintLayout

    private val sharedPrefFile = "LaunderUp"
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var userID:String
    private lateinit var clothOrderID:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root= inflater.inflate(R.layout.fragment_order, container, false)
        orderPlacedStatusBox=root.findViewById(R.id.order_placed_status_box)
        orderPlacedStatusLine=root.findViewById(R.id.order_placed_status_line)
        orderPickedStatusBox=root.findViewById(R.id.order_picked_status_box)
        orderPickedStatusLine=root.findViewById(R.id.order_picked_status_line)
        orderProcessingStatusBox=root.findViewById(R.id.order_processing_status_box)
        orderProcessingStatusLine=root.findViewById(R.id.order_processing_status_line)
        orderCompletedStatusBox=root.findViewById(R.id.order_completed_status_box)
        orderCompletedStatusLine=root.findViewById(R.id.order_completed_status_line)
        orderDeliveredStatusBox=root.findViewById(R.id.order_delivery_status_box)
        orderPlacedStatusTV=root.findViewById(R.id.order_placed_status_tv)
        orderPickedStatusTV=root.findViewById(R.id.order_picked_status_tv)
        orderProcessingStatusTV=root.findViewById(R.id.order_processing_status_tv)
        orderCompletedStatusTV=root.findViewById(R.id.order_completed_status_tv)
        orderDeliveredStatusTV=root.findViewById(R.id.order_delivered_status_tv)

        cancelOrderButton=root.findViewById(R.id.cancel_order_btn)
        orderStatusProgressIndicator=root.findViewById(R.id.order_status_progress)
        noOrderPlacedTextView=root.findViewById(R.id.no_order_placed_tv)
        orderStatusLayout=root.findViewById(R.id.order_status_layout)

        sharedPreferences=context!!.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        userID= sharedPreferences.getString("UID","").toString()
        clothOrderID= sharedPreferences.getString("clothOrderID","").toString()
        orderStatusLayout.isVisible=false

        if(clothOrderID.isEmpty()){
            noOrderPlacedTextView.isVisible=true
        }
        else {
            orderStatusProgressIndicator.show()
            getOrderDetails("Normal",clothOrderID)
        }


        cancelOrderButton.setOnClickListener {
            MaterialAlertDialogBuilder(context!!)
                .setTitle("Cancel Order")
                .setMessage("Are you sure you want to cancel order?")
                .setPositiveButton("Yes"){dialog,which->
                    getOrderDetails("Cancel Button",clothOrderID)

                }
                .setNegativeButton("No"){dialog,which->
                    dialog.dismiss()
                }
                .show()
        }

        return root
    }

    private fun getOrderDetails(status:String,clothOrderID:String){
        Log.i("Cloth Order ID",clothOrderID)
        val orderDetails= HerokuInstance.herokuapi.getOrderDetails(orderID=clothOrderID)
        orderDetails.enqueue(object : Callback<OrderData> {
            override fun onResponse(call: Call<OrderData>, response: Response<OrderData>) {
                orderStatusProgressIndicator.hide()
                orderStatusLayout.isVisible=true
                val orderStatus=response.body()!!.status
                if(orderStatus=="Picked_Up" || orderStatus=="Completed")
                    cancelOrderButton.isEnabled=false
//                if(status=="Cancel Button" && (response.body()!!.status=="Placed" || response.body()!!.status=="Accepted"))
//                    cancelOrder()
                if(response.code()==200 && status=="Normal")
                    updateOrderStatus(response.body()!!.status)

            }
            override fun onFailure(call: Call<OrderData>, t: Throwable) {
                Log.i(MainActivity::class.simpleName, "Order Details Fetch Failed")
            }
        })
    }

    private fun cancelOrder(){
        val cancelOrder= HerokuInstance.herokuapi.cancelOrder(uid = userID,clothOrderID=clothOrderID)
        cancelOrder.enqueue(object : Callback<CancelOrder> {
            override fun onResponse(call: Call<CancelOrder>, response: Response<CancelOrder>) {
                if(response.body()!!.result=="Order Cancelled" && response.code()==200)
                Toast.makeText(context,"Order Cancelled Successfully",Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call<CancelOrder>, t: Throwable) {
                Log.i(MainActivity::class.simpleName, "Order Cancel Failed")
            }
        })
    }

    private fun updateOrderStatus(orderStatus:String){
        when(orderStatus.lowercase().trim()){
            "placed"->{
                orderPlacedStatusBox.setColorFilter(Color.parseColor("#1C63E3"))
                orderPlacedStatusLine.setColorFilter(Color.parseColor("#1C63E3"))
                orderPlacedStatusTV.setTextColor((Color.parseColor("#1C63E3")))
            }

            "picked_up"->{
                orderPlacedStatusBox.setColorFilter(Color.parseColor("#1C63E3"))
                orderPlacedStatusLine.setColorFilter(Color.parseColor("#1C63E3"))
                orderPlacedStatusTV.setTextColor((Color.parseColor("#1C63E3")))
                orderPickedStatusBox.setColorFilter(Color.parseColor("#1C63E3"))
                orderPickedStatusLine.setColorFilter(Color.parseColor("#1C63E3"))
                orderPickedStatusTV.setTextColor((Color.parseColor("#1C63E3")))
                orderProcessingStatusBox.setColorFilter(Color.parseColor("#1C63E3"))
                orderProcessingStatusLine.setColorFilter(Color.parseColor("#1C63E3"))
                orderProcessingStatusTV.setTextColor((Color.parseColor("#1C63E3")))
            }

            "completed"->{
                orderPlacedStatusBox.setColorFilter(Color.parseColor("#1C63E3"))
                orderPlacedStatusLine.setColorFilter(Color.parseColor("#1C63E3"))
                orderPlacedStatusTV.setTextColor((Color.parseColor("#1C63E3")))
                orderPickedStatusBox.setColorFilter(Color.parseColor("#1C63E3"))
                orderPickedStatusLine.setColorFilter(Color.parseColor("#1C63E3"))
                orderPickedStatusTV.setTextColor((Color.parseColor("#1C63E3")))
                orderProcessingStatusBox.setColorFilter(Color.parseColor("#1C63E3"))
                orderProcessingStatusLine.setColorFilter(Color.parseColor("#1C63E3"))
                orderProcessingStatusTV.setTextColor((Color.parseColor("#1C63E3")))
                orderCompletedStatusBox.setColorFilter(Color.parseColor("#1C63E3"))
                orderCompletedStatusLine.setColorFilter(Color.parseColor("#1C63E3"))
                orderCompletedStatusTV.setTextColor((Color.parseColor("#1C63E3")))
                orderDeliveredStatusBox.setColorFilter(Color.parseColor("#1C63E3"))
                orderDeliveredStatusTV.setTextColor((Color.parseColor("#1C63E3")))
            }
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OrderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}