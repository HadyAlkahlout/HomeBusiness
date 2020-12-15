package com.nurbk.ps.homebusness.model.DataCategories.StoreCategory


import com.google.gson.annotations.SerializedName

data class StoreCategory(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)