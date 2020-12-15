package com.nurbk.ps.homebusness.model.mymessage


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Content(
    @SerializedName("id")
    var id: Int,
    @SerializedName("subject")
    var subject: String,
    @SerializedName("message")
    var message: String,
    @SerializedName("sender")
    var sender: String,
    @SerializedName("sender_image")
    var sender_image: String
) : Parcelable