package com.nurbk.ps.homebusness.model.profile


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UpdateProfile(
    @SerializedName("name")
    val name: String?,
    @SerializedName("email")
    val email: String?
):Parcelable