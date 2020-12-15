package com.nurbk.ps.homebusness.model.DataHome


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("down_banner")
    var downBanner: DownBanner,
    @SerializedName("middle_banner")
    var middleBanner: DownBanner,
    @SerializedName("middle_banner2")
    var middleBanner2: DownBanner,
    @SerializedName("middle_banner3")
    var middleBanner3: DownBanner,
    @SerializedName("new_markets")
    var newMarkets: List<SpecialMarket>,
    @SerializedName("new_products")
    var newProducts: List<NewProduct>,
    @SerializedName("special_markets")
    var specialMarkets: List<SpecialMarket>,
    @SerializedName("special_products")
    var specialProducts: List<NewProduct>,
    @SerializedName("stories")
    var stories: List<Stories>,
    @SerializedName("up_banner")
    var upBanner: DownBanner
)