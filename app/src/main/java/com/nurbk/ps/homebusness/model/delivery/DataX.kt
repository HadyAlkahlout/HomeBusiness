package com.nurbk.ps.homebusness.model.delivery


import com.google.gson.annotations.SerializedName

data class DataX(
    @SerializedName("id")
    var id: Int,
    @SerializedName("title")
    var title: String
)