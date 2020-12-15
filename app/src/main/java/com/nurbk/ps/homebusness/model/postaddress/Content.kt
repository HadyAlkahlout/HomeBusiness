package com.nurbk.ps.homebusness.model.postaddress

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Content (
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("city_id")
    val city_id: String,
    @SerializedName("region_id")
    val region_id: String,
    @SerializedName("lat")
    val lat: String,
    @SerializedName("lng")
    val lng: String,
    @SerializedName("street")
    val street: String,
    @SerializedName("block")
    val block: String,
    @SerializedName("floor")
    val floor: String,
    @SerializedName("address")
    val address:String,
    @SerializedName("flat")
    val flat: String,
    @SerializedName("building")
    val building: String,
    @SerializedName("avenue")
    val avenue: String,
    @SerializedName("mobile")
    val mobile: String

):Parcelable
