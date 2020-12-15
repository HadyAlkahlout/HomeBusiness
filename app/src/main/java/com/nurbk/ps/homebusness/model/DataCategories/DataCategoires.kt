package com.nurbk.ps.homebusness.model.DataCategories


import com.google.gson.annotations.SerializedName

data class DataCategoires(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: List<Data>,
    @SerializedName("status")
    var status: Boolean
)