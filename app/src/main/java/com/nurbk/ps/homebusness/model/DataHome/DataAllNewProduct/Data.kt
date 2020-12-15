package com.nurbk.ps.homebusness.model.DataHome.DataAllNewProduct


import com.google.gson.annotations.SerializedName
import com.nurbk.ps.homebusness.model.DataHome.NewProduct

data class Data(
    @SerializedName("count_total")
    var countTotal: Int,
    @SerializedName("data")
    var `data`: ArrayList<NewProduct>,
    @SerializedName("nextPageUrl")
    var nextPageUrl: Any,
    @SerializedName("pages")
    var pages: Int
)