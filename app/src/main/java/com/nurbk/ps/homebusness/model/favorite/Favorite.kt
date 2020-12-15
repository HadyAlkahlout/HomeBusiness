package com.nurbk.ps.homebusness.model.favorite


import com.google.gson.annotations.SerializedName
import com.nurbk.ps.homebusness.model.favorite.Data

data class Favorite(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)