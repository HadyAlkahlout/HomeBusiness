package com.nurbk.ps.homebusness.model.DataHome.DataAllNewStore


import com.google.gson.annotations.SerializedName

data class DataNewStore(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)