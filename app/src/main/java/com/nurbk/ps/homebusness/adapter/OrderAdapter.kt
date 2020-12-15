package com.nurbk.ps.homebusness.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.model.myorder.Content
import kotlinx.android.synthetic.main.item_design_order.view.*

class OrderAdapter(var data: ArrayList<Content>, var onClick: OnClickItem) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(item: View) : RecyclerView.ViewHolder(item)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_design_order, parent, false)
        )
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = data[position]

        holder.itemView.apply {
            txtNameOrder.text = order.user_name
            txtQuantity.text = order.id.toString()
            txtData.text = order.date
            txtPriceOrder.text = order.total_cost

            Glide.with(context).load(order.user_image).into(imageOrder)
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