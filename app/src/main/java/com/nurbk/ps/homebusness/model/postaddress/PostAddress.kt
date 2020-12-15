package com.nurbk.ps.homebusness.model.postaddress


import com.google.gson.annotations.SerializedName


data class PostAddress(
    @SerializedName("code")
    var code: Int,
    @SerializedName("status")
    var status: Boolean
)