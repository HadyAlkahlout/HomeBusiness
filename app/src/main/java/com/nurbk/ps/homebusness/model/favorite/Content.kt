package com.nurbk.ps.homebusness.model.favorite


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Content(
    @SerializedName("id")
    var id: Long,
    @SerializedName("title")
    var title: String,
    @SerializedName("note")
    var note: String,
    @SerializedName("price")
    var price: String,
    @SerializedName("category_id")
    var category_id: String,
    @SerializedName("category_title")
    var category_title: String,
    @SerializedName("image")
    var image: String

):Parcelable