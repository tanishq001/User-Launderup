package com.example.launderup.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.launderup.R
import com.example.launderup.data.models.Data

class ShopListAdapter (val context:Context, private val items:List<Data>):
    RecyclerView.Adapter<ShopListAdapter.ViewHolder>(){


    private lateinit var mListener:OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener=listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.shop_list_cardview,parent,false),mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item= items[position]
        holder.shopName.text=item.shop_name
        holder.shopAddress.text=item.shop_address
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view:View,listener: OnItemClickListener):RecyclerView.ViewHolder(view) {
        val shopImg:ImageView=view.findViewById(R.id.shop_img_iv)
        val shopName:TextView=view.findViewById(R.id.shop_name_tv)
        val shopAddress:TextView=view.findViewById(R.id.shop_address_tv)

        init {
            view.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

}