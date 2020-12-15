package com.nurbk.ps.homebusness.model.myorder


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Content(
    @SerializedName("id")
    var id: Long,
    @SerializedName("status_id")
    var status_id: String,
    @SerializedName("payment_id")
    var payment_id: String,
    @SerializedName("payment")
    val payment: String,
    @SerializedName("user_name")
    var user_name: String,
    @SerializedName("address")
    var address: String,
    @SerializedName("user_mobile")
    var user_mobile: String,
    @SerializedName("user_image")
    var user_image: String,
    @SerializedName("total_cost")
    var total_cost: String,
    @SerializedName("delivery_cost")
    var delivery_cost: String,
    @SerializedName("from_date")
    var from_date: String,
    @SerializedName("date")
    var date: String

):Parcelable