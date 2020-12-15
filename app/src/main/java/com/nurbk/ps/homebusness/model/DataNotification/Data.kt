package com.nurbk.ps.homebusness.model.DataNotification


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("count_total")
    var countTotal: Int,
    @SerializedName("data")
    var `data`: ArrayList<Notification>,
    @SerializedName("nextPageUrl")
    var nextPageUrl: String,
    @SerializedName("pages")
    var pages: Int,
    @SerializedName("un_read")
    var unRead: Int
)