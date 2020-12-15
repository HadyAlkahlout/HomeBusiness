package com.nurbk.ps.homebusness.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.model.cart.DataX
import kotlinx.android.synthetic.main.item_design_cart.view.*

class CartAdapter(var data: ArrayList<DataX>, var onClick: OnClickItem) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    var total = 0
    class CartViewHolder(item: View) : RecyclerView.ViewHolder(item)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_design_cart, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cart = data[position]

        holder.itemView.apply {
            txtNameCart.text = cart.title
            txt_cart_desc.text = cart.note
            txtprice.text = cart.price.toString()
             var counter = 1;

            total += cart.number * cart.price
            txt_cart_count.text = cart.number.toString()

            btn_increse.setOnClickListener {
                onClick.onClickListener(cart,0,holder.adapterPosition)
            }

            btn_decries.setOnClickListener {
                onClick.onClickListener(cart,1,holder.adapterPosition)
            }


        }

    }

    override fun getItemCount(): Int {
        return data.size
    }


    interface OnClickItem {
        fun onClickListener(order: DataX,type:Int,position: Int)
    }
}