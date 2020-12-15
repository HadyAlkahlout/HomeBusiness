package com.nurbk.ps.homebusness.model.privacy


import com.google.gson.annotations.SerializedName
import com.nurbk.ps.homebusness.model.privacy.Data

data class Privacy(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)