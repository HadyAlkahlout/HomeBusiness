package com.nurbk.ps.homebusness.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.model.Country.Data
import com.nurbk.ps.homebusness.model.Country.Region
import kotlinx.android.synthetic.main.item_expandable_region.view.*


class SpinnerRegionAdapter(var dataSource: List<*>, var type: Int) : BaseAdapter() {

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(i: Int): Any {
        return when (type) {
            1 -> (dataSource[i] as Data)
            3 -> (dataSource[i] as com.nurbk.ps.homebusness.model.delivery.DataX)
            else -> (dataSource[i] as com.nurbk.ps.homebusness.model.DataCategories.Data)
        }
    }

    override fun getItemId(i: Int): Long {
        Log.e("hdhd", "getItemId: ${dataSource[i]}")
        return when (type) {
            1 -> (dataSource[i] as Data).id.toLong()
            3 -> (dataSource[i] as com.nurbk.ps.homebusness.model.delivery.DataX).id.toLong()
            else -> (dataSource[i] as com.nurbk.ps.homebusness.model.DataCategories.Data).id.toLong()
        }
    }

    @SuppressLint("ViewHolder")
    override fun getView(i: Int, view: View?, vg: ViewGroup?): View {


        val rowItem = getItem(i)

        val root = LayoutInflater.from(vg!!.context)
            .inflate(R.layout.item_expandable_region, vg, false)

        root.txt.text = when (type) {
            1 -> (dataSource[i] as Data).title
            3 -> (dataSource[i] as com.nurbk.ps.homebusness.model.delivery.DataX).title
            else -> (dataSource[i] as com.nurbk.ps.homebusness.model.DataCategories.Data).title
        }


        return root
    }
}