package com.nurbk.ps.homebusness.model.delivery


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("data")
    var `data`: List<DataX>
)