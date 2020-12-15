package com.raiyansoft.eata.model.user


import com.google.gson.annotations.SerializedName
import com.nurbk.ps.homebusness.model.user.Data

data class UserToken(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)