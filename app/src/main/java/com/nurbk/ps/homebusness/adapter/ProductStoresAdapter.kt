package com.nurbk.ps.homebusness.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.databinding.ItemDesignProductStoreBinding
import com.nurbk.ps.homebusness.model.StoreDetails.MostSell
import com.nurbk.ps.homebusness.model.StoreDetails.Product
import com.nurbk.ps.homebusness.util.Constant
import kotlinx.android.synthetic.main.item_design_product_store.view.*


class ProductStoresAdapter(val data: ArrayList<Product>, val onClick: OnClickListener) :
    RecyclerView.Adapter<ProductStoresAdapter.CategoryStoresViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryStoresViewHolder {
        return CategoryStoresViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_design_product_store, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryStoresViewHolder, position: Int) {

        val dataMarket = data[position]

        holder.itemView.apply {

            Constant.setImage(
                context,
                dataMarket.image,
                imageProduct,
                R.drawable.store_placeholder
            )

            txtNameProduct.text = dataMarket.title
            txtDecProduct.text = dataMarket.note

            val price = resources.getString(R.string.dk, dataMarket.price)
            txtPriceProduct.text = price

            if (dataMarket.fav) {
                btnFavFav.setImageResource(R.drawable.ic_icon_awesome_heart)
            } else {
                btnFavFav.setImageResource(R.drawable.ic_baseline_favorite_border_24)

            }


            btnFavFav.setOnClickListener {
                if (Constant.getSharePref(context).getString(Constant.TOKEN, "") == null ||
                    Constant.getSharePref(context).getString(Constant.TOKEN, "").toString().isEmpty()
                ) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.singin),
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }
                onClick.addFav(dataMarket, position)
                if (dataMarket.fav) {
                    btnFavFav.setImageResource(R.drawable.ic_baseline_favorite_border_24)

                } else {
                    btnFavFav.setImageResource(R.drawable.ic_icon_awesome_heart)

                }
            }

            btnAddCartFav.setOnClickListener {
                onClick.onClickItem(dataMarket, 2)
            }
            setOnClickListener {
                onClick.onClickItem(dataMarket, 1)
            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class CategoryStoresViewHolder(item: ItemDesignProductStoreBinding) :
        RecyclerView.ViewHolder(item.root)


    interface OnClickListener {
        fun onClickItem(data: Product, type: Int)
        fun addFav(data: Product, position: Int)
    }
}