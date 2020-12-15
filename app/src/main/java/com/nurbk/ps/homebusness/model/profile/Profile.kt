package com.nurbk.ps.homebusness.model.profile


import com.google.gson.annotations.SerializedName
import com.nurbk.ps.homebusness.model.profile.Data

data class Profile(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)