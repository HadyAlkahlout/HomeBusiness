package com.nurbk.ps.homebusness.model.packages

import com.google.gson.annotations.SerializedName

class Packages(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)