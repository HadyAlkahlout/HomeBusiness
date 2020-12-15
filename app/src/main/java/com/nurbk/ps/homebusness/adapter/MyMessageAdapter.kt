package com.nurbk.ps.homebusness.adapter


import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.databinding.ItemDesignHomeMealBinding
import com.nurbk.ps.homebusness.databinding.ItemDesignNewMealBinding
import com.nurbk.ps.homebusness.databinding.ItemMessageBinding
import com.nurbk.ps.homebusness.databinding.ItemNotificationBinding
import com.nurbk.ps.homebusness.model.DataHome.NewProduct
import com.nurbk.ps.homebusness.model.DataNotification.Notification
import com.nurbk.ps.homebusness.model.mymessage.Content
import com.nurbk.ps.homebusness.model.mymessage.Message
import com.nurbk.ps.homebusness.util.Constant
import kotlinx.android.synthetic.main.item_design_home_meal.view.*
import kotlinx.android.synthetic.main.item_design_new_meal.view.*
import kotlinx.android.synthetic.main.item_message.view.*
import kotlinx.android.synthetic.main.item_notification.view.*
import kotlinx.android.synthetic.main.item_notification.view.txtMessage

class MyMessageAdapter(
    val activity:Activity,
    val data: ArrayList<Content>,
    var onClickListener: OnClickListener
) :
    RecyclerView.Adapter<MyMessageAdapter.MealViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        return MealViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_message, parent, false
            )

        )
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {


        val message = data[position]





        holder.itemView.apply {
            messageTitle.text = message.subject
            txtMessage.text = message.message

            Constant.setImage(activity,message.sender_image,circularImageView,R.drawable.ic_profile_default)

            setOnClickListener {
                onClickListener.onClickItem(message,position)
            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MealViewHolder(item: ItemMessageBinding) :
        RecyclerView.ViewHolder(
            item.root
        )

    interface OnClickListener {
        fun onClickItem(data: Content, position: Int)
    }


}