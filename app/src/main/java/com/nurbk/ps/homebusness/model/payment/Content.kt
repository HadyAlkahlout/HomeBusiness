package com.nurbk.ps.homebusness.model.payment

import com.google.gson.annotations.SerializedName

class Content(
    @SerializedName("id")
    var id: Long,
    @SerializedName("title")
    var title: String,
    @SerializedName("online_payment")
    var online_payment: String

)