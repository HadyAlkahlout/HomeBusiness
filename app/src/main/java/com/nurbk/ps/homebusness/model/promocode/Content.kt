package com.nurbk.ps.homebusness.model.promocode

import com.google.gson.annotations.SerializedName

class Content(
        @SerializedName("code")
        var code: Long,
        @SerializedName("type")
        var type: String,
        @SerializedName("discount_type")
        var discount_type: String,
        @SerializedName("discount")
        var discount: String

)