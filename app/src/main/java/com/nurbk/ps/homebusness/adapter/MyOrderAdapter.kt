package com.nurbk.ps.homebusness.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.model.myorder.Content
import kotlinx.android.synthetic.main.item_design_order.view.*
import kotlinx.android.synthetic.main.item_sent_requests.view.*

class MyOrderAdapter(var data: ArrayList<Content>, var onClick: OnClickItem) :
    RecyclerView.Adapter<MyOrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(item: View) : RecyclerView.ViewHolder(item)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_sent_requests, parent, false)
        )
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = data[position]

        holder.itemView.apply {
            item_order_id.text = order.id.toString()
            item_order_name.text = order.user_name
            item_order_address.text = order.address
            item_order_cost.text = order.total_cost

            setOnClickListener {
                onClick.onClickListener(order)
            }

        }

    }

    override fun getItemCount(): Int {
        return data.size
    }


    interface OnClickItem {
        fun onClickListener(order: Content)
    }
}