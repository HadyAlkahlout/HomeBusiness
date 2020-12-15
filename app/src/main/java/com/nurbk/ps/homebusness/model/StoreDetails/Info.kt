package com.nurbk.ps.homebusness.model.StoreDetails


import com.google.gson.annotations.SerializedName

data class Info(
    @SerializedName("allow_call")
    var allowCall: Int,
    @SerializedName("allow_whats")
    var allowWhats: Int,
    @SerializedName("date")
    var date: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("image")
    var image: String,
    @SerializedName("min_order")
    var minOrder: Int,
    @SerializedName("mobile")
    var mobile: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("note")
    var note: String,
    @SerializedName("rate")
    var rate: Int,
    @SerializedName("views")
    var views: Int,
    @SerializedName("follow")
    var follow: Boolean
)