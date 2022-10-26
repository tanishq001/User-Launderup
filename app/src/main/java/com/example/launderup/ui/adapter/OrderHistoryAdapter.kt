package com.example.launderup.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.launderup.R
import com.example.launderup.data.models.OrderData

class OrderHistoryAdapter (val context: Context, private val items:List<OrderData>):
    RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>(){


    private lateinit var mListener:OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener=listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.orders_cardview,parent,false),mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item= items[position]
      holder.serviceType.text=item.service_type
        holder.deliveryDate.text=item.delivery_dt
        holder.orderAddress.text=item.address
        holder.orderTotalCost.text=item.total_cost

    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View, listener: OnItemClickListener): RecyclerView.ViewHolder(view) {
        val serviceType: TextView =view.findViewById(R.id.service_type_tv)
        val deliveryDate: TextView =view.findViewById(R.id.delivery_date_tv)
        val orderAddress: TextView =view.findViewById(R.id.address_tv)
        val orderTotalCost: TextView =view.findViewById(R.id.total_cost_tv)

        init {
            view.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

}