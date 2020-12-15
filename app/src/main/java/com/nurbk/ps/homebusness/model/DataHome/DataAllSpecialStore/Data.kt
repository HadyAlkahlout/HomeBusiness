package com.nurbk.ps.homebusness.model.DataHome.DataAllSpecialStore


import com.google.gson.annotations.SerializedName
import com.nurbk.ps.homebusness.model.DataHome.SpecialMarket

data class Data(
    @SerializedName("count_total")
    var countTotal: Int,
    @SerializedName("data")
    var `data`: ArrayList<SpecialMarket>,
    @SerializedName("nextPageUrl")
    var nextPageUrl: Any,
    @SerializedName("pages")
    var pages: Int
)