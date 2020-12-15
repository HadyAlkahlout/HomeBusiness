package com.nurbk.ps.homebusness.model.StoreDetails


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("categories")
    var categories: List<Category>,
    @SerializedName("info")
    var info: Info,
    @SerializedName("most_sells")
    var mostSells: List<Product>,
    @SerializedName("reviews")
    var reviews: List<Review>
)