package com.nurbk.ps.homebusness.model.ProductDetails


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("category_id")
    var categoryId: String,
    @SerializedName("category_title")
    var categoryTitle: String,
    @SerializedName("code")
    var code: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("images")
    var images: ArrayList<Image>,
    @SerializedName("note")
    var note: String,
    @SerializedName("price")
    var price: Int,
    @SerializedName("title")
    var title: String,
    @SerializedName("user_id")
    var userId: Int,
    var fav: Boolean
)