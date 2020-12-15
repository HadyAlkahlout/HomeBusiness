package com.nurbk.ps.homebusness.model.status


import com.google.gson.annotations.SerializedName

data class Status(
    @SerializedName("code")
    var code: Int,
    @SerializedName("status")
    var status: Boolean
)