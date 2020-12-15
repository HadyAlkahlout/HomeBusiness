package com.nurbk.ps.homebusness.model.address


import com.google.gson.annotations.SerializedName
import com.nurbk.ps.homebusness.model.address.Data


data class Address(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)