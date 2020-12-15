package com.nurbk.ps.homebusness.model.DataHome


import com.google.gson.annotations.SerializedName

data class NewProduct(
    @SerializedName("bathes")
    var bathes: Any,
    @SerializedName("category_id")
    var categoryId: String,
    @SerializedName("category_title")
    var categoryTitle: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("image")
    var image: String,
    @SerializedName("rooms")
    var rooms: Any,
    @SerializedName("size")
    var size: Any,
    @SerializedName("title")
    var title: String
)
