package com.nurbk.ps.homebusness.model.DataHome


import com.google.gson.annotations.SerializedName

data class SpecialMarket(
    @SerializedName("id")
    var id: Int,
    @SerializedName("image")
    var image: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("rate")
    var rate: String,
    @SerializedName("min_order")
    var min_order: String,
)