package com.nurbk.ps.homebusness.model.myproduct


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Content(
    @SerializedName("category_id")
    var categoryId: String,
    @SerializedName("category_title")
    var categoryTitle: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("status")
    var status: Int,
    @SerializedName("image")
    var images: String,
    @SerializedName("note")
    var note: String,
    @SerializedName("price")
    var price: String,
    @SerializedName("title")
    var title: String
) : Parcelable