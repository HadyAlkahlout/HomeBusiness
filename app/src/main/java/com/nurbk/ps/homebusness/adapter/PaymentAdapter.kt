package com.nurbk.ps.homebusness.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.databinding.ItemPaymentBinding
import com.nurbk.ps.homebusness.model.payment.Content
import kotlinx.android.synthetic.main.item_payment.view.*


class PaymentAdapter(
    var activity: Activity, var data: MutableList<Content>, val itemclic: onClick
) :
    RecyclerView.Adapter<PaymentAdapter.MyViewHolder>() {

    var last_position =0

    class MyViewHolder(val item: ItemPaymentBinding) : RecyclerView.ViewHolder(item.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView_layout: ItemPaymentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_payment, parent, false
        )
        return MyViewHolder(itemView_layout)
    }

    override fun getItemCount(): Int {
        return data.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = data[position]
      //  holder.bind(currentItem)

        if (position != last_position) {
            holder.itemView.radio_cash.isChecked = false
        }else{
            holder.itemView.radio_cash.isChecked = true
        }

        holder.itemView.apply {


            radio_cash.text=currentItem.title
            radio_cash.setOnClickListener {
                radio_cash.isChecked =true
                last_position = holder.adapterPosition
                itemclic.onClick(currentItem,holder.adapterPosition,1)
            }

        }


    }

    interface onClick {
        fun onClick(data:Content,position: Int, type: Int)
    }


}
