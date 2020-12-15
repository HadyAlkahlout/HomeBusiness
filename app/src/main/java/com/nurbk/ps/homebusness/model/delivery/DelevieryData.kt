package com.nurbk.ps.homebusness.model.delivery


import com.google.gson.annotations.SerializedName

data class DelevieryData(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)