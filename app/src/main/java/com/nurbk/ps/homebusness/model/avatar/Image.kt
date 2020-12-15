package com.nurbk.ps.homebusness.model.avatar


import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("image")
    var image: String,
    @SerializedName("id")
    var id: String
)