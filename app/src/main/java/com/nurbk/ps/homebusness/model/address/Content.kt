package com.nurbk.ps.homebusness.model.address

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Content (
        @SerializedName("id")
        val id: String?,
        @SerializedName("title")
        val title: String,
        @SerializedName("city_id")
        var city_id: String,
        @SerializedName("region_id")
        var region_id: String,
        @SerializedName("lat")
        var lat: String,
        @SerializedName("lng")
        var lng: String,
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
