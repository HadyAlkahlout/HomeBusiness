package com.nurbk.ps.homebusness.model.Country


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Region(
    @SerializedName("id")
    var id: Int,
    @SerializedName("title")
    var title: String,
    var price: String,
) : Parcelable