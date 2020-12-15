package com.nurbk.ps.homebusness.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.databinding.ItemDesignSortMBinding
import com.nurbk.ps.homebusness.model.Country.Country
import com.nurbk.ps.homebusness.model.Country.Data
import com.nurbk.ps.homebusness.model.Country.Region
import kotlinx.android.synthetic.main.item_design_sort_m.view.*

class AddCityAdapter(
    val data: ArrayList<Any>,
    var type: Int,
    val onClick: OnClickItemListener
) :
    RecyclerView.Adapter<AddCityAdapter.MyViewHolder>() {

    class MyViewHolder(item: ItemDesignSortMBinding) : RecyclerView.ViewHolder(item.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_design_sort_m, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        var country: Data? = null
        var region: Region? = null
        if (type == 1)
            country = data[position] as Data
        else
            region = data[position] as Region

        holder.itemView.apply {
            name.text = if (type == 1) country!!.title else region!!.title
            price.text = if (type == 1) country!!.price else region!!.price

            btnDelete.setOnClickListener {
                onClick.onClick(if (type == 1) country!! else region!!, type)
            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface OnClickItemListener {
        fun onClick(item: Any, type: Int)
    }
}