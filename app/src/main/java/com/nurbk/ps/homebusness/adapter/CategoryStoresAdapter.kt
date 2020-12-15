package com.nurbk.ps.homebusness.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.databinding.ItemDesignCategoryStoreBinding
import com.nurbk.ps.homebusness.model.DataHome.SpecialMarket
import com.nurbk.ps.homebusness.util.Constant
import kotlinx.android.synthetic.main.item_design_category.view.*
import kotlinx.android.synthetic.main.item_design_category.view.imgCategoryIcon
import kotlinx.android.synthetic.main.item_design_category_store.view.*


class CategoryStoresAdapter(val data: ArrayList<SpecialMarket>, val onClick: OnClickListener) :
    RecyclerView.Adapter<CategoryStoresAdapter.CategoryStoresViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryStoresViewHolder {
        return CategoryStoresViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_design_category_store, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryStoresViewHolder, position: Int) {

        val dataMarket = data[position]

        holder.itemView.apply {

            Constant.setImage(
                context,
                dataMarket.image,
                imgStorePhoto,
                R.drawable.store_placeholder
            )

            tvStoreName.text = dataMarket.name
            tvLowestPrice.text = dataMarket.min_order
            rbStoreRate.rating = dataMarket.rate.toFloat();
            rbStoreRate.isEnabled = false

            setOnClickListener {
                onClick.onClickItem(dataMarket)
            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class CategoryStoresViewHolder(item: ItemDesignCategoryStoreBinding) :
        RecyclerView.ViewHolder(item.root)


    interface OnClickListener {
        fun onClickItem(data: SpecialMarket)
    }
}