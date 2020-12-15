package com.nurbk.ps.homebusness.model.profile


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Data(
    @SerializedName("allow_call")
    val allowCall: Int?,
    @SerializedName("allow_whats")
    val allowWhats: Int?,
    @SerializedName("avatar")
    val avatar: String?,
    @SerializedName("cat_id")
    val catId: Int?,
    @SerializedName("cities")
    val cities: ArrayList<City>?,
    @SerializedName("country_code")
    val countryCode: String?,
    @SerializedName("delivery")
    val delivery: Int?,
    @SerializedName("delivery_time")
    val deliveryTime: Int?,
    @SerializedName("lat")
    val lat: String?,
    @SerializedName("lng")
    val lng: String?,
    @SerializedName("market")
    var market: Boolean?,
    @SerializedName("market_status")
    val marketStatus: Int?,
    @SerializedName("min_order")
    val minOrder: Int?,
    @SerializedName("mobile")
    val mobile: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("note")
    val note: String?,
    @SerializedName("open_from")
    val openFrom: String?,
    @SerializedName("open_to")
    val openTo: String?,
    @SerializedName("rate")
    val rate: Int?,
    @SerializedName("regions")
    val regions: List<Region>?,
    @SerializedName("send_notification")
    val sendNotification: Boolean?,
    @SerializedName("sub_cat_id")
    val subCatId: Int?,
    @SerializedName("sub_cat_id_2")
    val subCatId2: Int?,
    @SerializedName("user_id")
    val userId: Int?,
    @SerializedName("views")
    val views: Int?
) : Parcelable