package com.nurbk.ps.homebusness.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.model.favorite.Content
import com.nurbk.ps.homebusness.util.Constant
import kotlinx.android.synthetic.main.item_design_favorite.view.*
import kotlinx.android.synthetic.main.item_design_home_story.view.*

class FavoriteAdapter(val data: ArrayList<Content>, var onClick: OnClickItem) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    class FavoriteViewHolder(item: View) : RecyclerView.ViewHolder(item)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_design_favorite, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favorite = data[position]

        holder.itemView.apply {
            txtNameFav.text = favorite.title
            txtDecFav.text = favorite.note
            txtPriceFav.text = favorite.price

            btnAddCartFav.setOnClickListener {
                onClick.onClickListener(favorite, position, 2)
            }
            btnFavFav.setOnClickListener {
                onClick.onClickListener(favorite, position, 3)
            }

            setOnClickListener {
                onClick.onClickListener(favorite, position, 1)
            }

            Constant.setImage(
                context,
                data[position].image,
                imageFav,
                R.drawable.store_placeholder
            )

        }

    }

    override fun getItemCount(): Int {
        return data.size
    }


    interface OnClickItem {
        fun onClickListener(content: Content, position: Int, type: Int)
    }
}