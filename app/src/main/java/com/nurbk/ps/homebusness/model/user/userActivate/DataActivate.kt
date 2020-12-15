package com.nurbk.ps.homebusness.model.user.userActivate


import com.google.gson.annotations.SerializedName
import com.nurbk.ps.homebusness.model.user.userActivate.Data

data class DataActivate(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)