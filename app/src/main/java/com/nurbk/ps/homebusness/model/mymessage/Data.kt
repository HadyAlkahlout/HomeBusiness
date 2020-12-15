package com.nurbk.ps.homebusness.model.mymessage


import com.google.gson.annotations.SerializedName

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