package com.example.launderup.ui.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.launderup.R
import com.example.launderup.ui.view.activities.MainActivity
import com.google.android.material.snackbar.Snackbar
import org.json.JSONArray

class ClothListAdapter (val context: Context, private val items:JSONArray):
    RecyclerView.Adapter<ClothListAdapter.ViewHolder>(){

    private var totalCost:Int=0
    private var totalCount:Int=0
    private lateinit var view:View
    private lateinit var snackBar: Snackbar
    private val sharedPrefFile = "LaunderUp"
    private lateinit var sharedPreferences: SharedPreferences



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        view=LayoutInflater.from(context).inflate(R.layout.cloth_list_cardview,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item= items.getJSONObject(position)
        val rate=item.getString("rate")
        var counting=0
        holder.clothName.text=item.getString("name")
        holder.clothRate.text= "Rs. $rate"

        if(counting<=0)
            holder.countDecrementBtn.isEnabled=false
        holder.countIncrementBtn.setOnClickListener {
            if(counting>0)
                holder.countDecrementBtn.isEnabled=true
            counting++
            holder.count.text="$counting"
            item.put("quantity",counting.toString())
            totalCost+=rate.toInt()
            totalCount++
            Toast.makeText(context, "Total Cost:$totalCost      Total Count:$totalCount ${item.getString("quantity")}",Toast.LENGTH_SHORT).show()
            if(totalCount==1)
            makeSnackBar()
            else
                snackBar.setText("Total Cost: $totalCost     Total Items: $totalCount")
        }
        holder.countDecrementBtn.setOnClickListener {
            if(counting<=0) {
                holder.countDecrementBtn.isEnabled=false
                counting = 0
            }
            else {
                counting--
                totalCount--
                totalCost-=rate.toInt()
            }
            holder.count.text="$counting"
            item.put("quantity",counting.toString())
            Toast.makeText(context, "Total Cost:$totalCost      Total Count:$totalCount ${item.getString("quantity")}",Toast.LENGTH_SHORT ).show()
            if(totalCount==0)
                snackBar.dismiss()
            else
                snackBar.setText("Total Cost: $totalCost     Total Items: $totalCount")
        }
    }

    override fun getItemCount(): Int {
        return items.length()
    }


    private fun makeSnackBar(){
        snackBar=Snackbar.make(view,"Total Cost: $totalCost     Total Items: $totalCount",Snackbar.LENGTH_INDEFINITE)
            .setAction("Next"){
                val clothes:JSONArray=cleanList()
                sharedPreferences=context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
                val editor:SharedPreferences.Editor=sharedPreferences.edit()
                if(sharedPreferences.getString("ClothesArray","")=="[]")
                    editor.putString("ClothesArray","null")
                else
                    editor.putString("ClothesArray",clothes.toString())
                        .putString("totalCost",totalCost.toString())
                editor.apply()
                editor.commit()
                val intent=Intent(context,MainActivity::class.java)
                    .putExtra("Cart",true)
                context.startActivity(intent)
            }
            .setBackgroundTint(Color.parseColor("#007ACC"))
            .setActionTextColor(Color.parseColor("#D4D4D4"))
            .setTextColor(Color.parseColor("#FFFFFF"))
        snackBar.show()
    }

    private fun cleanList():JSONArray{
        for(i in 0 until items.length()) {
            val cloth = items.getJSONObject(i)
            if (cloth.getString("quantity") == "null" || cloth.getString("quantity")=="0")
                items.remove(i)
        }
        return items
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val clothImg: ImageView =view.findViewById(R.id.cloth_img)
        val clothName: TextView =view.findViewById(R.id.cloth_name)
        val clothRate: TextView =view.findViewById(R.id.cloth_rate)
        val countIncrementBtn: Button =view.findViewById(R.id.count_increment_btn)
        val countDecrementBtn: Button =view.findViewById(R.id.count_decrement_btn)
        var count:Button=view.findViewById(R.id.count)
    }
}