package com.nurbk.ps.homebusness.model.profile


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(
    @SerializedName("city_id")
    val cityId: Int?,
    @SerializedName("price")
    val price: Int?
):Parcelable