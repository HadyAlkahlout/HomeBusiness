package com.nurbk.ps.homebusness.model.ProductDetails


import com.google.gson.annotations.SerializedName

data class ProductDetails(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)