package com.nurbk.ps.homebusness.model.myorder


import com.google.gson.annotations.SerializedName
import com.nurbk.ps.homebusness.model.myorder.Content

data class Data(
    @SerializedName("count_total")
    var countTotal: Int,
    @SerializedName("count")
    var count: Int,
    @SerializedName("data")
    var `data`: ArrayList<Content>,
    @SerializedName("nextPageUrl")
    var nextPageUrl: Any,
    @SerializedName("pages")
    var pages: Int
)