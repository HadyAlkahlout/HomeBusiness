package com.nurbk.ps.homebusness.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.TextView
import com.nurbk.ps.homebusness.R
import com.nurbk.ps.homebusness.model.Country.Data
import com.nurbk.ps.homebusness.model.Country.Region


class MyExpandableClass(
    var context: Context,
    var expandableListView: ExpandableListView,
    var header: ArrayList<Data>,
    var body: ArrayList<List<Region>>,
    val itemclick: onClick
) : BaseExpandableListAdapter() {

    override fun getGroup(groupPosition: Int): String {
        return header[groupPosition].title
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View? {


        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val convertView = inflater.inflate(R.layout.item_expandable_group_city, null)

        val title = convertView?.findViewById<TextView>(R.id.txt_group)
        title?.text = getGroup(groupPosition)
        title?.setOnClickListener {
            if (expandableListView.isGroupExpanded(groupPosition))
                expandableListView.collapseGroup(groupPosition)
            else
                expandableListView.expandGroup(groupPosition)
        }
        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return body[groupPosition].size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): String {
        val data = body[groupPosition][childPosition]
        return data.title
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View? {


        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val convertView = inflater.inflate(R.layout.item_expandable_region, null)

        val title = convertView?.findViewById<TextView>(R.id.txt)
        title?.text = getChild(groupPosition, childPosition)
        convertView!!.setOnClickListener {
            itemclick.onClickItem(
                header[groupPosition], body[groupPosition][childPosition]
            )
        }
        return convertView
    }


    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return header.size
    }

    interface onClick {
        fun onClickItem(city: Data, region: Region)
    }
}