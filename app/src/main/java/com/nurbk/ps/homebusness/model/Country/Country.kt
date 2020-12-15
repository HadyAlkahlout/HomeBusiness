package com.nurbk.ps.homebusness.model.Country


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Country(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: List<Data>,
    @SerializedName("status")
    var status: Boolean,
) : Parcelable