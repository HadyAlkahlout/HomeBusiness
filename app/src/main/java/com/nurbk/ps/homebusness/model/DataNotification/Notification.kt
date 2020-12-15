package com.nurbk.ps.homebusness.model.DataNotification


import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("created_at")
    var createdAt: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("read")
    var read: Int,
    @SerializedName("title")
    var title: String
)