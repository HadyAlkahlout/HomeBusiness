package com.nurbk.ps.homebusness.model.DataHome


import com.google.gson.annotations.SerializedName

data class DataHome(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)