package com.nurbk.ps.homebusness.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nurbk.ps.homebusness.R
import kotlinx.android.synthetic.main.item_design_my_product.view.*
import kotlinx.android.synthetic.main.item_myproduct.view.*

class MyProductAdapter(activity: Activity,var data: ArrayList<com.nurbk.ps.homebusness.model.myproduct.Content>, var onClick: OnClickItem,var type:Int) :
    RecyclerView.Adapter<MyProductAdapter.OrderViewHolder>() {

    class OrderViewHolder(item: View) : RecyclerView.ViewHolder(item)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            if (type == 0) {
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_design_my_product, parent, false)
            }else{
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_myproduct, parent, false)
            }
        )
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val product = data[position]

        holder.itemView.apply {
            if (type == 0) {
                txtNameProduct.text = product.title
                txt_note.text = product.note
                txtPrice_product.text = product.price +" دك "

                Glide.with(context).load(product.images).into(imageProduct)
            }else{
                txt_name.text = product.title
                txt_desc.text = product.note
                txt_price.text = product.price+" دك "

                Glide.with(context).load(product.images).into(imageView)

                if (product.status == 1){
                    btn_switch.isChecked =true
                }

                btn_switch.setOnCheckedChangeListener { buttonView, isChecked ->
                    onClick.onClickListener(product,2)
                }

                textView5.setOnClickListener {
                    onClick.onClickListener(product,3)
                }
            }
            setOnClickListener {
                onClick.onClickListener(product,1)
            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }


    interface OnClickItem {
        fun onClickListener(order: com.nurbk.ps.homebusness.model.myproduct.Content,type: Int)
    }
}