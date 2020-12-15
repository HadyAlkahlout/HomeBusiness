package com.nurbk.ps.homebusness.model.profile


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Region(
    @SerializedName("price")
    val price: Int?,
    @SerializedName("region_id")
    val regionId: Int?
):Parcelable