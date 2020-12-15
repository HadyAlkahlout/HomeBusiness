package com.nurbk.ps.homebusness.model.myproduct


import com.google.gson.annotations.SerializedName
import com.nurbk.ps.homebusness.model.myorder.Content

data class Data(
    @SerializedName("count_total")
    var countTotal: Int,
    @SerializedName("data")
    var `data`: List<com.nurbk.ps.homebusness.model.myproduct.Content>,
    @SerializedName("nextPageUrl")
    var nextPageUrl: Any,
    @SerializedName("pages")
    var pages: Int
)