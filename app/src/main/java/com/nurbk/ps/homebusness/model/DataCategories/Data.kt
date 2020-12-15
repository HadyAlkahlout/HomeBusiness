package com.nurbk.ps.homebusness.model.DataCategories


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("id")
    var id: Int,
    @SerializedName("image")
    var image: String,
    @SerializedName("subCategory")
    var subCategory: Int,
    @SerializedName("title")
    var title: String
)