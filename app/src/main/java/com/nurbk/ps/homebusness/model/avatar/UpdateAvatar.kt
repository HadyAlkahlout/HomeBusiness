package com.nurbk.ps.homebusness.model.avatar


import com.google.gson.annotations.SerializedName

data class UpdateAvatar(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("status")
    val status: Boolean?
)