package com.nurbk.ps.homebusness.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.model.Order
import com.nurbk.ps.homebusness.model.cart.DataX
import kotlinx.android.synthetic.main.item_checkout_product.view.*

class CheckoutAdapter(var data: ArrayList<DataX>) :
    RecyclerView.Adapter<CheckoutAdapter.AddressViewHolder>() {

    class AddressViewHolder(item: View) : RecyclerView.ViewHolder(item)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_checkout_product, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val checkout = data[position]

        holder.itemView.apply {
            item_product_quantity.text =null
            item_product_name.text =checkout.title
            item_product_price.text =checkout.price.toString()

        }

    }

    override fun getItemCount(): Int {
        return data.size
    }




}