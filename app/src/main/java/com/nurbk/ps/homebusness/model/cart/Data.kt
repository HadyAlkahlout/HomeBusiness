package com.nurbk.ps.homebusness.model.cart


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("addresses")
    val addresses: Addresses?,
    @SerializedName("data")
    val `data`: List<DataX>?
)