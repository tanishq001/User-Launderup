package com.example.launderup.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.launderup.R

class AddressAdapter (val context: Context):
    RecyclerView.Adapter<AddressAdapter.ViewHolder>(){

    private lateinit var mListener:OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener=listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.address_cardview,parent,false),mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item= items.getJSONObject(position)
//        var counting=0
//        holder.clothName.text=item.getString("name")
//        holder.clothRate.text="Rs. "+item.getString("rate")
//        holder.countIncrementBtn.setOnClickListener {
//            counting++
//            holder.count.text="$counting"
//        }
//        holder.countDecrementBtn.setOnClickListener {
//            counting--
//            if(counting<0)
//                counting=0
//            holder.count.text="$counting"
//        }

    }

    override fun getItemCount(): Int {
//        return items.length()
        return 0
    }



    class ViewHolder(view: View, listener: OnItemClickListener): RecyclerView.ViewHolder(view) {
        val clothImg: ImageView =view.findViewById(R.id.cloth_img)
        val clothName: TextView =view.findViewById(R.id.cloth_name)
        val clothRate: TextView =view.findViewById(R.id.cloth_rate)
        val countIncrementBtn: Button =view.findViewById(R.id.count_increment_btn)
        val countDecrementBtn: Button =view.findViewById(R.id.count_decrement_btn)
        var count: Button =view.findViewById(R.id.count)

        init {
            view.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

}