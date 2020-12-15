package com.nurbk.ps.homebusness.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.databinding.ItemDesignStoreReviewBinding
import com.nurbk.ps.homebusness.model.StoreDetails.MostSell
import com.nurbk.ps.homebusness.model.StoreDetails.Review
import com.nurbk.ps.homebusness.model.packages.Content
import com.nurbk.ps.homebusness.util.Constant
import kotlinx.android.synthetic.main.fragment_details_store.view.*
import kotlinx.android.synthetic.main.item_design_store_review.view.*
import kotlinx.android.synthetic.main.item_dialog_btn.view.*


class DialogItemAdapter(var data: ArrayList<Content>, val onClick: OnClickListener) :
    RecyclerView.Adapter<DialogItemAdapter.DialogViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogViewHolder {
        return DialogViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_dialog_btn, parent, false)
                )
    }

    override fun onBindViewHolder(holder: DialogViewHolder, position: Int) {

        val item = data[position]

        holder.itemView.apply {

            tv_title.text =item.title
            tv_price.text =item.price+" دك "

            setOnClickListener {
                onClick.onClickItem(item,item.id)
            }

        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    class DialogViewHolder(item: View) : RecyclerView.ViewHolder(item)



    interface OnClickListener {
        fun onClickItem(data: Content,id:Long)
    }
}