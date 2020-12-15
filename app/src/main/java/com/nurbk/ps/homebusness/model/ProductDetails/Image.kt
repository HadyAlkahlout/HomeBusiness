package com.nurbk.ps.homebusness.model.ProductDetails


import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("id")
    var id: Int,
    @SerializedName("image")
    var image: String
)