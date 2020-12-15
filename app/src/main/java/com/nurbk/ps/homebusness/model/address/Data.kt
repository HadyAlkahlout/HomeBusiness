package com.nurbk.ps.homebusness.model.address


import com.google.gson.annotations.SerializedName
import com.nurbk.ps.homebusness.model.postaddress.Content

data class Data(
    @SerializedName("data")
    var `data`: ArrayList<com.nurbk.ps.homebusness.model.address.Content>
)