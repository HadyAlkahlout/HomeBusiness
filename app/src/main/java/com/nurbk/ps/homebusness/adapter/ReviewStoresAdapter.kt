package com.nurbk.ps.homebusness.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.databinding.ItemDesignStoreReviewBinding
import com.nurbk.ps.homebusness.model.StoreDetails.MostSell
import com.nurbk.ps.homebusness.model.StoreDetails.Review
import com.nurbk.ps.homebusness.util.Constant
import kotlinx.android.synthetic.main.fragment_details_store.view.*
import kotlinx.android.synthetic.main.item_design_store_review.view.*


class ReviewStoresAdapter(val data: ArrayList<Review>, val onClick: OnClickListener) :
    RecyclerView.Adapter<ReviewStoresAdapter.CategoryStoresViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryStoresViewHolder {
        return CategoryStoresViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_design_store_review, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryStoresViewHolder, position: Int) {

        val review = data[position]

        holder.itemView.apply {

            Constant.setImage(
                context,
                review.image,
                imageReview,
                R.drawable.store_placeholder
            )

            txtDesReview.text = review.comment
            txtName.text = review.userName

            txtReview.text = review.rate.toString()


            setOnClickListener {
                onClick.onClickItem(review)
            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class CategoryStoresViewHolder(item: ItemDesignStoreReviewBinding) :
        RecyclerView.ViewHolder(item.root)


    interface OnClickListener {
        fun onClickItem(data: Review)
    }
}