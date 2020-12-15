package com.nurbk.ps.homebusness.model.DataHome.DataAllNewProduct


import com.google.gson.annotations.SerializedName

data class DataNewProduct(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)