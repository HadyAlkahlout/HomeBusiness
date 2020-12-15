package com.nurbk.ps.homebusness.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.databinding.ItemDeisgnStoreListBinding
import com.nurbk.ps.homebusness.model.ClassID
import com.nurbk.ps.homebusness.model.StoreDetails.Category
import com.nurbk.ps.homebusness.model.StoreDetails.MostSell
import com.nurbk.ps.homebusness.model.StoreDetails.Product
import com.nurbk.ps.homebusness.model.StoreDetails.Review
import com.nurbk.ps.homebusness.ui.viewmodel.ProductDetailsViewModel
import com.nurbk.ps.homebusness.util.Constant
import kotlinx.android.synthetic.main.item_deisgn_store_list.view.*


class ListStoresAdapter(val data: ArrayList<Category>, val onClick: OnClickListener) :
    RecyclerView.Adapter<ListStoresAdapter.CategoryStoresViewHolder>(),
    ProductStoresAdapter.OnClickListener {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryStoresViewHolder {
        return CategoryStoresViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_deisgn_store_list, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryStoresViewHolder, position: Int) {

        val category = data[position]

        holder.itemView.apply {

            txtName.text = category.title


            val mAdapter = ProductStoresAdapter(ArrayList(), this@ListStoresAdapter)
            mAdapter.data.clear()
            mAdapter.data.addAll(category.products)
            mAdapter.notifyDataSetChanged()
            if (mAdapter.data.size == 0) {
                txtName.visibility = View.GONE
            }

            rcDataList.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = mAdapter
            }

        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class CategoryStoresViewHolder(item: ItemDeisgnStoreListBinding) :
        RecyclerView.ViewHolder(item.root)


    interface OnClickListener {
        fun onClickItem(data: Product, type: Int)
        fun addFav(data: Product, position: Int)
    }


    override fun onClickItem(data: Product, type: Int) {
        onClick.onClickItem(data, type)
    }

    override fun addFav(data: Product, position: Int) {
        onClick.addFav(data, position)
    }
}