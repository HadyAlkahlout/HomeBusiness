package com.nurbk.ps.homebusness.model.mymessage


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("status")
    val status: Boolean
)