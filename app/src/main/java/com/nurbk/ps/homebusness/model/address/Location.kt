package com.nurbk.ps.homebusness.model.address

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location(val latlng: LatLng):Parcelable