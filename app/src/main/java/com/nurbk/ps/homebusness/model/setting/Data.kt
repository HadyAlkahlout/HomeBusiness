package com.nurbk.ps.homebusness.model.setting


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("force_close")
    var forceClose: String,
    @SerializedName("android_version")
    var android_version: String,
    @SerializedName("ios_version")
    var ios_version: String,
    @SerializedName("force_update")
    var forceUpdate: String,
    @SerializedName("special_market")
    var special_market: String,
    @SerializedName("special_product")
    var special_product: String,
    @SerializedName("market_upgrade")
    var market_upgrade: String

)