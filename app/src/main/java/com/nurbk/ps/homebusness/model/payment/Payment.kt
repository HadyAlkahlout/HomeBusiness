package com.nurbk.ps.homebusness.model.payment

import com.google.gson.annotations.SerializedName

class Payment(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: List<Content>,
    @SerializedName("status")
    var status: Boolean
)