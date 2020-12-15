package com.nurbk.ps.homebusness.model.DataHome.DataAllSpecialProduct


import com.google.gson.annotations.SerializedName

data class DataAllSpecialProduct(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)