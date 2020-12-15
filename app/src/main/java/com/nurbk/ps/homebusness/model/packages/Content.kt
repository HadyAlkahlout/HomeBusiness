package com.nurbk.ps.homebusness.model.packages

import com.google.gson.annotations.SerializedName

class Content(
    @SerializedName("id")
    var id: Long,
    @SerializedName("title")
    var title: String,
    @SerializedName("price")
    var price: String

)