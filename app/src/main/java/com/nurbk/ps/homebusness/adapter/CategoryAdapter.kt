package com.nurbk.ps.homebusness.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.databinding.ItemDesignCategoryBinding
import com.nurbk.ps.homebusness.model.DataCategories.Data
import com.nurbk.ps.homebusness.util.Constant
import kotlinx.android.synthetic.main.item_design_category.view.*



class CategoryAdapter(val data: ArrayList<Data>,val onClick:OnClickItem) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_design_category, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val dataCategories = data[position]

        holder.itemView.apply {
            Constant.setImage(
                context,
                dataCategories.image,
                imgCategoryIcon,
                R.drawable.store_placeholder
            )
            tvCategoryName.text = dataCategories.title

            setOnClickListener {
                onClick.onClick(dataCategories)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class CategoryViewHolder(item: ItemDesignCategoryBinding) :
        RecyclerView.ViewHolder(item.root)

    interface OnClickItem {
        fun onClick(dataCategoires: Data)
    }

}