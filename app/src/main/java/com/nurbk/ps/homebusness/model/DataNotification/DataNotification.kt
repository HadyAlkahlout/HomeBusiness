package com.nurbk.ps.homebusness.model.DataNotification


import com.google.gson.annotations.SerializedName

data class DataNotification(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)