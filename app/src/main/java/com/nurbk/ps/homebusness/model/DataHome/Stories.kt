package com.nurbk.ps.homebusness.model.DataHome


import com.google.gson.annotations.SerializedName

data class Stories(
        @SerializedName("id")
        var id: Int,
        @SerializedName("image")
        var image: String,
        @SerializedName("market_id")
        var marketId: Int,
        @SerializedName("market_name")
        var marketName: String,
        @SerializedName("market_image")
        var marketImage:String
)