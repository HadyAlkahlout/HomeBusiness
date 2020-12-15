package com.nurbk.ps.homebusness.model.promocode

import com.google.gson.annotations.SerializedName

class PromoCode(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Content,
    @SerializedName("status")
    var status: Boolean
)