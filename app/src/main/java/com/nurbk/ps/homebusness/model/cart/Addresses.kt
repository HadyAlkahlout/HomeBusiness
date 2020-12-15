package com.nurbk.ps.homebusness.model.cart


import com.google.gson.annotations.SerializedName

data class Addresses(
    @SerializedName("cities")
    val cities: List<City>?,
    @SerializedName("regions")
    val regions: List<Region>?
)