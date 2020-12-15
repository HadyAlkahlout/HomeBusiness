package com.nurbk.ps.homebusness.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.databinding.ItemDesignHomeMealBinding
import com.nurbk.ps.homebusness.databinding.ItemDesignNewMealBinding
import com.nurbk.ps.homebusness.databinding.ItemNotificationBinding
import com.nurbk.ps.homebusness.model.DataHome.NewProduct
import com.nurbk.ps.homebusness.model.DataNotification.Notification
import com.nurbk.ps.homebusness.util.Constant
import kotlinx.android.synthetic.main.item_design_home_meal.view.*
import kotlinx.android.synthetic.main.item_design_new_meal.view.*
import kotlinx.android.synthetic.main.item_notification.view.*

class NotificationAdapter(
    val data: ArrayList<Notification>,
    var onClickListener: OnClickListener
) :
    RecyclerView.Adapter<NotificationAdapter.MealViewHolder>() {
    var counterRead = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        return MealViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_notification, parent, false
            )

        )
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {


        val notification = data[position]





        holder.itemView.apply {
            titleNotification.text = notification.title
            txtMessage.text = notification.message
            readNotification.tag = position
            if (notification.read == 0) {
                readNotification.visibility = View.VISIBLE
            } else {
                readNotification.visibility = View.GONE
            }

            setOnClickListener {
                onClickListener.onClickItem(notification, readNotification.tag as Int)
            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MealViewHolder(item: ItemNotificationBinding) :
        RecyclerView.ViewHolder(
            item.root
        )

    interface OnClickListener {
        fun onClickItem(data: Notification, position: Int)
    }
}