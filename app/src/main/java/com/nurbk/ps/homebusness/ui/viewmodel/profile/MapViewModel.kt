package com.nurbk.ps.homebusness.ui.viewmodel.profile

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

class MapViewModel(application: Application) : AndroidViewModel(application) {



    val dataMapLiveData = MutableLiveData<LatLng>()
    val dataMapStatusLiveData = MutableLiveData<Int>()


    init {
        getLocation(application)
    }

    fun getLocation(context: Context) {
        val locationclint =
            LocationServices.getFusedLocationProviderClient(context)
        locationclint.lastLocation
            .addOnSuccessListener { location ->
                if (location == null) {
                    dataMapStatusLiveData.postValue(0)
                }else{
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val location = LatLng(latitude, longitude)
                    Log.e("eee location", location.toString())
                    val userFileMap = mutableMapOf<String, Any>()
                    Log.e("eee latitude :", latitude.toString())
                    userFileMap["location"] =
                        latitude.toString() + "," + longitude.toString()
                    dataMapLiveData.postValue(location)
                    dataMapStatusLiveData.postValue(1)
                }
            }
    }

}