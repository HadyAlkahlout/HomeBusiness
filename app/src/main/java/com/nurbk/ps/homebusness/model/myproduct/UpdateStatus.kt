package com.nurbk.ps.homebusness.model.myproduct

import com.google.gson.annotations.SerializedName

class UpdateStatus(
    @SerializedName("id")
    var id: Int,
    @SerializedName("status")
    var status: Int
)