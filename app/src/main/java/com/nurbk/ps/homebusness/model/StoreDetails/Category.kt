package com.nurbk.ps.homebusness.model.StoreDetails


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id")
    var id: Int,
    @SerializedName("products")
    var products: List<Product>,
    @SerializedName("title")
    var title: String
)