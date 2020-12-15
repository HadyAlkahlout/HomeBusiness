package com.nurbk.ps.homebusness.model.question


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("data")
    var `data`: List<Question>
)