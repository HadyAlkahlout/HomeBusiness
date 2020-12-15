package com.nurbk.ps.homebusness.model.favorite


import com.google.gson.annotations.SerializedName
import com.nurbk.ps.homebusness.model.favorite.Content

data class Data(
    @SerializedName("count_total")
    var countTotal: Int,
    @SerializedName("data")
    var `data`: ArrayList<Content>,
    @SerializedName("nextPageUrl")
    var nextPageUrl: Any,
    @SerializedName("pages")
    var pages: Int
)