package com.nurbk.ps.homebusness.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.databinding.ItemDesignHomeMealBinding
import com.nurbk.ps.homebusness.databinding.ItemDesignNewMealBinding
import com.nurbk.ps.homebusness.model.DataHome.NewProduct
import com.nurbk.ps.homebusness.model.DataHome.SpecialMarket
import com.nurbk.ps.homebusness.util.Constant
import kotlinx.android.synthetic.main.item_design_home_meal.view.*
import kotlinx.android.synthetic.main.item_design_home_store.view.*
import kotlinx.android.synthetic.main.item_design_new_meal.view.*

class HomeMealAdapter(val data: ArrayList<NewProduct>, var type: Int,var onClickListener:OnClickListener) :
    RecyclerView.Adapter<HomeMealAdapter.MealViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        return MealViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                if (type == 4 || type == 3) R.layout.item_design_new_meal
                else
                    R.layout.item_design_home_meal, parent, false
            )

        )
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {


        val newProduct = data[position]





        holder.itemView.apply {

            Constant.setImage(
                context,
                newProduct.image,
                if (type == 4 || type == 3) imgMealNew else imgMeal,
                R.drawable.store_placeholder
            )
            if (type == 4 || type == 3)
                tvMealNameNew.text =
                    newProduct.title
            else
                tvMealName.text =
                    newProduct.title

            setOnClickListener {
                onClickListener.onClickItem(newProduct)
            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MealViewHolder(item: Any) :
        RecyclerView.ViewHolder(
            if (type == 4 || type == 3) (item as ItemDesignNewMealBinding).root else
                (item as ItemDesignHomeMealBinding).root
        )

    interface OnClickListener {
        fun onClickItem(data: NewProduct)
    }
}