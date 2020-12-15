package com.nurbk.ps.homebusness.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.databinding.ItemImageSliderBinding
import com.nurbk.ps.homebusness.model.ProductDetails.Image
import com.nurbk.ps.homebusness.util.Constant
import kotlinx.android.synthetic.main.item_design_home_story.view.*
import kotlinx.android.synthetic.main.item_design_home_story.view.imgStory
import kotlinx.android.synthetic.main.item_image_slider.view.*


class ImageSliderAdapter(
    var data: ArrayList<Image>
) :
    RecyclerView.Adapter<ImageSliderAdapter.MyViewHolder>() {


    class MyViewHolder(item: ItemImageSliderBinding) : RecyclerView.ViewHolder(item.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_image_slider, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.itemView.apply {
            Constant.setImage(
                context,
                data[position].image,
                tv_image_slider,
                R.drawable.store_placeholder
            )
        }


    }


    interface onClick {
        fun onClickItem(position: Int, type: Int)
    }


}
