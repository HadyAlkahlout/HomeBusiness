package com.nurbk.ps.homebusness.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.databinding.ItemDesignHomeStoreBinding
import com.nurbk.ps.homebusness.databinding.ItemDesignNewStoreBinding
import com.nurbk.ps.homebusness.model.DataHome.NewProduct
import com.nurbk.ps.homebusness.model.DataHome.SpecialMarket
import com.nurbk.ps.homebusness.model.DataHome.Stories
import com.nurbk.ps.homebusness.util.Constant.setImage
import kotlinx.android.synthetic.main.item_design_home_store.view.*
import kotlinx.android.synthetic.main.item_design_new_store.view.*


class HomeStoreAdapter(val data: ArrayList<Any>, var type: Int,var onClick:OnClickListener) :
    RecyclerView.Adapter<HomeStoreAdapter.MealViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        return MealViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                if (type == 4 ||type==3)
                    R.layout.item_design_new_store
                else
                    R.layout.item_design_home_store, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {

        val specialMarket: SpecialMarket = data[position] as SpecialMarket

        holder.itemView.apply {
            setImage(
                context,
              specialMarket.image ,
                if (type == 4||type==3) imgStoreNew else imgStore,
                R.drawable.store_placeholder
            )

            setOnClickListener {
                onClick.onClickItem(specialMarket)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MealViewHolder(item: Any) :
        RecyclerView.ViewHolder(
            if (type == 4 ||type==3) (item as ItemDesignNewStoreBinding).root
            else
                (item as ItemDesignHomeStoreBinding).root
        )


    interface OnClickListener {
        fun onClickItem(data: SpecialMarket)
    }
}