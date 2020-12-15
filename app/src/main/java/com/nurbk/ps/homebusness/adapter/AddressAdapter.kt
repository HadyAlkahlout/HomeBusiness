package com.nurbk.ps.homebusness.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.model.address.Content
import kotlinx.android.synthetic.main.item_design_address.view.*

class AddressAdapter(val data: ArrayList<Content>, var onClick: OnClickItem) :
    RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    class AddressViewHolder(item: View) : RecyclerView.ViewHolder(item)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_design_address, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = data[position]

        holder.itemView.apply {
            titleAddress.text = address.title
            txtDecAddress.text = "${address.lat},${address.lng}"

            setOnClickListener {
                onClick.onClickListener(address)
            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }


    interface OnClickItem {
        fun onClickListener(data: Content)
    }
}