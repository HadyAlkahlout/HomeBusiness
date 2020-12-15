package com.nurbk.ps.homebusness.model.DataHome


import com.google.gson.annotations.SerializedName

data class DownBanner(
    @SerializedName("image")
    var image: String,
    @SerializedName("url")
    var url: String
)