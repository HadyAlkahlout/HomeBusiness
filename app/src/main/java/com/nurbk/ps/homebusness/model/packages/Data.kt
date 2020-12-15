package com.nurbk.ps.homebusness.model.packages

import com.google.gson.annotations.SerializedName

class Data(
    @SerializedName("data")
    var data: List<Content>
)