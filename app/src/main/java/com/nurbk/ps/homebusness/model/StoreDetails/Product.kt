package com.nurbk.ps.homebusness.model.StoreDetails


import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("category_id")
    var categoryId: String,
    @SerializedName("category_title")
    var categoryTitle: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("image")
    var image: String,
    @SerializedName("note")
    var note: String,
    @SerializedName("price")
    var price: Int,
    @SerializedName("title")
    var title: String,
    @SerializedName("fav")
    var fav:Boolean
)