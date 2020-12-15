package com.nurbk.ps.homebusness.model.StoreDetails


import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("comment")
    var comment: String,
    @SerializedName("image")
    var image: String,
    @SerializedName("rate")
    var rate: Int,
    @SerializedName("user_id")
    var userId: Int,
    @SerializedName("user_name")
    var userName: String
)