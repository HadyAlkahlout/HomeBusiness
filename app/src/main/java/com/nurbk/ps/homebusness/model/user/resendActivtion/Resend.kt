package com.nurbk.ps.homebusness.model.user.resendActivtion


import com.google.gson.annotations.SerializedName
import com.nurbk.ps.homebusness.model.user.resendActivtion.Data

data class Resend(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)