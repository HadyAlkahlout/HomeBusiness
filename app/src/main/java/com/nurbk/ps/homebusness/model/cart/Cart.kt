package com.nurbk.ps.homebusness.model.cart


import com.google.gson.annotations.SerializedName

data class Cart(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("status")
    val status: Boolean
)