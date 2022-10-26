package com.example.launderup.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.launderup.R
import org.json.JSONArray

class CartClothListAdapter(val context: Context, private val items: JSONArray):
    RecyclerView.Adapter<CartClothListAdapter.ViewHolder>(){

    private lateinit var mListener:OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.cart_cloth_list_cardview,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item= items.getJSONObject(position)
        val name=item.getString("name")
        val rate=item.getString("rate").toInt()
        val quantity=item.getString("quantity").toInt()
        val totalCost=rate*quantity
        var counting=quantity

        holder.clothName.text=name
        holder.clothRate.text= "₹ $rate"
        holder.clothTotalCost.text= "₹ $totalCost"
        if(counting<10) holder.count.text="0$counting"
        else holder.count.text="$counting"

        holder.countDecrementBtn.setOnClickListener {
            if(counting<=0) counting=0
            else counting--

            if(counting<10) holder.count.text="0$counting"
            else holder.count.text="$counting"

            holder.clothTotalCost.text="₹ "+(rate*counting).toString()
            item.put("quantity",counting.toString())
            Toast.makeText(context,"Decremented by1 ",Toast.LENGTH_SHORT).show()
        }

        holder.countIncrementBtn.setOnClickListener {
            counting++
            if(counting<10)
                holder.count.text="0$counting"
            else
                holder.count.text="$counting"

            holder.clothTotalCost.text="₹ "+(rate*counting).toString()
            item.put("quantity",counting.toString())
            Toast.makeText(context,"Incremented by1 ",Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount(): Int {
        return items.length()
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val clothName: TextView =view.findViewById(R.id.cart_cloth_name)
        val clothRate: TextView =view.findViewById(R.id.cart_cloth_rate)
        val clothTotalCost:TextView=view.findViewById(R.id.cart_cloth_totalCost)
        val countIncrementBtn: Button =view.findViewById(R.id.count_increment_btn)
        val countDecrementBtn: Button =view.findViewById(R.id.count_decrement_btn)
        var count: TextView =view.findViewById(R.id.count_tv)


    }

    fun getFinalList():JSONArray{
        return items
    }

    fun totalCost():Int{
        var totalCost:Int=0
        for(i in 0 until items.length()){
            val totalQuantity=items.getJSONObject(i).getString("quantity").toInt()
            val rate=items.getJSONObject(i).getString("rate").toInt()
            totalCost+=(rate*totalQuantity)
        }
        return totalCost
    }

}