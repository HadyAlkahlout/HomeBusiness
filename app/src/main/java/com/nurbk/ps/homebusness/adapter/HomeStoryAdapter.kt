package com.nurbk.ps.homebusness.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.databinding.ItemDesignHomeStoryBinding
import com.nurbk.ps.homebusness.model.DataHome.Stories
import com.nurbk.ps.homebusness.util.Constant
import kotlinx.android.synthetic.main.item_design_home_story.view.*


class HomeStoryAdapter(val data: ArrayList<Stories>, val onView: OnViewStory) :
    RecyclerView.Adapter<HomeStoryAdapter.StoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {

        return StoryViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_design_home_story, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.itemView.apply {
            Constant.setImage(
                context,
                data[position].marketImage,
                imgStory,
                R.drawable.store_placeholder
            )


            setOnClickListener {
                onView.onView(data[position])
            }

        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class StoryViewHolder(item: ItemDesignHomeStoryBinding) :
        RecyclerView.ViewHolder(item.root)

    interface OnViewStory {
        fun onView(data: Stories)
    }
}