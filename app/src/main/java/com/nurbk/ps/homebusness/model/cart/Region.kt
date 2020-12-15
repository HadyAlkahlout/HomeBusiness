package com.nurbk.ps.homebusness.model.cart


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Region(
    @SerializedName("region_id")
    var region_id: Int,
    @SerializedName("title")
    var title: String,
    var price: String,
) : Parcelable