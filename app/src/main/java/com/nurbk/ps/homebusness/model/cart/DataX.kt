package com.nurbk.ps.homebusness.model.cart


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataX(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("item_id")
    val itemId: Int?,
    @SerializedName("market_id")
    val marketId: Int?,
    @SerializedName("market_name")
    val marketName: String?,
    @SerializedName("note")
    val note: String?,
    @SerializedName("number")
    var number: Int,
    @SerializedName("price")
    val price: Int,
    @SerializedName("title")
    val title: String?
):Parcelable