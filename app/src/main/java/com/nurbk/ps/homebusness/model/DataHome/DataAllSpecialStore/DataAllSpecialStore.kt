package com.nurbk.ps.homebusness.model.DataHome.DataAllSpecialStore


import com.google.gson.annotations.SerializedName

data class DataAllSpecialStore(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)