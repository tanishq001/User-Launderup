package com.example.launderup.ui.view.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.launderup.R
import com.example.launderup.ui.adapter.CartClothListAdapter
import com.example.launderup.ui.view.activities.ConfirmDetailsActivity
import com.example.launderup.ui.view.activities.MainActivity
import com.example.launderup.ui.view.activities.PaymentMethodActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import org.json.JSONArray
import org.w3c.dom.Text
import java.util.*
import kotlin.time.Duration.Companion.days

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class CartFragment : Fragment() {
    private lateinit var placeOrderButton: Button
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var clothesLayout:ConstraintLayout
    private lateinit var billSummaryLayout: ConstraintLayout
    private lateinit var orderCancellationPolicyLayout: ConstraintLayout
    private lateinit var emptyCartTv:TextView
    private lateinit var cartTV:TextView
    private lateinit var itemTotalPriceTv:TextView
    private lateinit var taxesTv:TextView
    private lateinit var orderTotalTv:TextView
    private lateinit var dateLayout:LinearLayout
    private lateinit var timeLayout:LinearLayout
    private lateinit var dateTv:TextView
    private lateinit var timeTv:TextView
    private lateinit var cartClothListAdapter:CartClothListAdapter

    private val sharedPrefFile = "LaunderUp"
    private lateinit var sharedPreferences: SharedPreferences

    private var totalOrderCost:Double=0.0
    private var totalCost:Double=0.0
    private var totalTaxes:Double=0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root= inflater.inflate(R.layout.fragment_cart, container, false)
        placeOrderButton=root.findViewById(R.id.place_button)
        cartRecyclerView=root.findViewById(R.id.cart_rv)
        clothesLayout=root.findViewById(R.id.cart_layout)
        emptyCartTv=root.findViewById(R.id.empty_cart_tv)
        cartTV=root.findViewById(R.id.cart_text)
        dateLayout=root.findViewById(R.id.date_layout)
        timeLayout=root.findViewById(R.id.time_layout)
        dateTv=root.findViewById(R.id.date_tv)
        timeTv=root.findViewById(R.id.time_tv)
        itemTotalPriceTv=root.findViewById(R.id.bill_summary_item_total_price)
        taxesTv=root.findViewById(R.id.bill_summary_taxes_price)
        orderTotalTv=root.findViewById(R.id.bill_summary_total_price)
        sharedPreferences=context!!.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        val clothesList: String? =sharedPreferences.getString("ClothesArray",null)

        if(clothesList==null) {
            emptyCartTv.isVisible = true
            clothesLayout.isVisible = false
        }
        else {
            totalCost=sharedPreferences.getString("totalCost","")!!.toDouble()
            emptyCartTv.isVisible=false
            clothesLayout.isVisible = true
            val clothes=JSONArray(clothesList)
            cartRecyclerView.layoutManager=LinearLayoutManager(context)
            cartClothListAdapter = CartClothListAdapter(context!!,clothes)
            cartRecyclerView.adapter=cartClothListAdapter
            itemTotalPriceTv.text="₹ $totalCost"
            totalTaxes=totalCost*0.18
            taxesTv.text="₹ $totalTaxes"
            totalOrderCost=totalCost*0.18+totalCost
            orderTotalTv.text="₹ $totalOrderCost"
        }


        placeOrderButton.setOnClickListener {
            //startActivity(Intent(context,ConfirmDetailsActivity::class.java))
            val finalList:JSONArray=cartClothListAdapter.getFinalList()
            Log.i("Clothes List",finalList.toString())
            itemTotalPriceTv.text="₹ "+cartClothListAdapter.totalCost().toFloat().toString()
            taxesTv.text="₹ "+(cartClothListAdapter.totalCost()*0.18).toString()
            totalOrderCost=cartClothListAdapter.totalCost()*0.18+cartClothListAdapter.totalCost()
            orderTotalTv.text="₹ $totalOrderCost"
            removeNullItems(finalList)

        }
        val calendar= Calendar.getInstance()
        val year=calendar.get(Calendar.YEAR)
        val month=calendar.get(Calendar.MONTH)
        val day=calendar.get(Calendar.DAY_OF_MONTH)
        val hour=calendar.get(Calendar.HOUR)
        val minute=calendar.get(Calendar.MINUTE)

        dateLayout.setOnClickListener {
            val constraintsBuilder =
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now())
            val datePicker=MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Pickup Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(constraintsBuilder.build())
                .build()
                datePicker.show(childFragmentManager,"Date Picker")



            datePicker.addOnPositiveButtonClickListener {
                dateTv.text=datePicker.headerText
            }

            datePicker.addOnCancelListener{
                it.dismiss()
            }
        }

        timeLayout.setOnClickListener{

            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(hour)
                .setMinute(minute)
                .setTitleText("Select Pickup Time")
                .build()
            timePicker.show(childFragmentManager,"Time Picker")


            timePicker.addOnPositiveButtonClickListener {
                val formattedTime: String = when {
                    timePicker.hour > 12 -> {
                        if (timePicker.minute < 10)
                            "0${timePicker.hour - 12}:0${timePicker.minute} PM"
                        else
                            "0${timePicker.hour - 12}:${timePicker.minute} PM"
                    }
                    timePicker.hour  == 12 -> {
                        if (timePicker.minute < 10)
                            "${timePicker.hour}:0${timePicker.minute} PM"
                         else
                            "${timePicker.hour}:${timePicker.minute} PM"
                    }
                    timePicker.hour == 0 -> {
                        if (timePicker.minute < 10)
                            "${timePicker.hour + 12}:0${timePicker.minute} AM"
                         else
                            "${timePicker.hour + 12}:${timePicker.minute} AM"
                    }
                    else -> {
                        if (timePicker.minute < 10)
                            "0${timePicker.hour}:0${timePicker.minute} AM"
                         else
                            "0${timePicker.hour}:${timePicker.minute} AM"
                    }
                }
                timeTv.text=formattedTime
            }
        }
        return root
    }

    private fun removeNullItems(clothesList:JSONArray) {
        for(i in 0 until clothesList.length()){
            val cloth=clothesList.getJSONObject(i)
            if(cloth.getString("quantity")=="null")
                clothesList.remove(i)
        }
        val editor:SharedPreferences.Editor=sharedPreferences.edit()
        editor.putString("ClothesArray",clothesList.toString())
        editor.apply()
        editor.commit()
        val intent=Intent(context, ConfirmDetailsActivity::class.java)
            .putExtra("totalOrderCost",totalOrderCost.toString())
            .putExtra("pickupDate","abc")
            .putExtra("pickupTime","abc")
            .putExtra("clothesTypes",clothesList.toString())

        Log.i("Cart Fragment Details",totalOrderCost.toString())
        Log.i("Cart Fragment Details",clothesList.toString())
        startActivity(intent)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}