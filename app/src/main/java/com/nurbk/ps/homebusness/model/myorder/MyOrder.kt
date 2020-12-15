package com.nurbk.ps.homebusness.model.myorder


import com.google.gson.annotations.SerializedName
import com.nurbk.ps.homebusness.model.myorder.Data

data class MyOrder(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)