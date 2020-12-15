package com.nurbk.ps.homebusness.model.user


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("token")
    var token: String,
    @SerializedName("user_id")
    var userId: Int
)