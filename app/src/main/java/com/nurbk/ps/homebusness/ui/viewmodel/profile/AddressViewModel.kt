package com.nurbk.ps.homebusness.ui.viewmodel.profile

import android.Manifest
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.nurbk.ps.homebusness.model.address.Address
import com.nurbk.ps.homebusness.model.postaddress.Content
import com.nurbk.ps.homebusness.model.postaddress.PostAddress
import com.nurbk.ps.homebusness.model.status.Status
import com.nurbk.ps.homebusness.repoistory.ApiRepository
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class AddressViewModel(application: Application):AndroidViewModel(application){


    val TAG = "AddressViewModel"

    private val repository = ApiRepository()

    val dataAddressLiveData = MutableLiveData<Resource<Address>>()
    private var address: Address? = null


    val dataPostAddressLiveData = MutableLiveData<Resource<PostAddress>>()
    private var postaddress: PostAddress? = null


    val dataDeleteAddressLiveData = MutableLiveData<Resource<Status>>()
    val dataGetPrimistionLiveData = MutableLiveData<Boolean>()
    private var status: Status? = null

    val dataUpdateAddressLiveData = MutableLiveData<Resource<Status>>()

    private val share by lazy {
        Constant.getSharePref(application.applicationContext)
    }

    private suspend fun getAddress(
        Authorization: String,lang:String
    ) {
        dataAddressLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getAddress(Authorization,lang)

            dataAddressLiveData.postValue(getAddress(response))
            Timber.e("$TAG getAddress-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataAddressLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getAddress-> Network Failure")
                }
                else -> {
                    dataAddressLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getAddress-> Conversion Error")
                }

            }
        }
    }

    private fun getAddress(response: Response<Address>):
            Resource<Address> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->

                Timber.e("$TAG v->page->$address")
                    address = resultResponse
                    Timber.d("$TAG getAddress->Favorite->$address")

                Timber.e("$TAG getAddress-> Resource.Success->$resultResponse")
                return Resource.Success(address ?: resultResponse)
            }
        }
        Timber.e("$TAG getAddress->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun getAddress() = viewModelScope.launch {
        getAddress(
            share.getString(Constant.TOKEN,"").toString(),"ar"
        )
    }





    private suspend fun PostAddress(
        donate: Content, Authorization: String
    ) {
        dataPostAddressLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .postAddress(donate, Authorization, share.getString(Constant.LANG, "ar").toString())

            dataPostAddressLiveData.postValue(PostAddress(response))
            Timber.d("$TAG getDonation-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataPostAddressLiveData.postValue(Resource.Error("Network Failure"))
                    Timber.e("$TAG getDonation-> Network Failure")
                }
                else -> {
                    dataPostAddressLiveData.postValue(Resource.Error("Conversion Error"))
                    Timber.e("$TAG getDonation-> Conversion Error")
                }

            }
        }
    }

    private fun PostAddress(response: Response<PostAddress>):
            Resource<PostAddress> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG getDonation->")
                postaddress = resultResponse
                Timber.d("$TAG getDonation->response->$postaddress")
                Timber.d("$TAG getDonation-> Resource.Success->$resultResponse")
                return Resource.Success(postaddress ?: resultResponse)
            }
        }
        Timber.e("$TAG getDonation->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun PostAddress(address: Content) =
        viewModelScope.launch {
            PostAddress(
                address,share.getString(Constant.TOKEN,"").toString()
            )
        }




    private suspend fun UpdateAddress(
        donate: Content, Authorization: String
    ) {
        dataUpdateAddressLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .updateAddress(donate, Authorization, share.getString(Constant.LANG, "ar").toString())

            dataUpdateAddressLiveData.postValue(UpdateAddress(response))
            Timber.d("$TAG getDonation-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataUpdateAddressLiveData.postValue(Resource.Error("Network Failure"))
                    Timber.e("$TAG getDonation-> Network Failure")
                }
                else -> {
                    dataUpdateAddressLiveData.postValue(Resource.Error("Conversion Error"))
                    Timber.e("$TAG getDonation-> Conversion Error")
                }

            }
        }
    }

    private fun UpdateAddress(response: Response<Status>):
            Resource<Status> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG getDonation->")
                status = resultResponse
                Timber.d("$TAG getDonation->response->$status")
                Timber.d("$TAG getDonation-> Resource.Success->$resultResponse")
                return Resource.Success(status ?: resultResponse)
            }
        }
        Timber.e("$TAG getDonation->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun UpdateAddress(address: Content) =
        viewModelScope.launch {
            UpdateAddress(
                address,share.getString(Constant.TOKEN,"").toString()
            )
        }





    private suspend fun deleteCase(
        Authorization: String, id:String

    ) {
        dataDeleteAddressLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .deleteAddress(Authorization,"ar",id)

            dataDeleteAddressLiveData.postValue(deleteCase(response))
            Timber.d("$TAG deleteCase-> OK")

        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataDeleteAddressLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG deleteCase-> Network Failure")
                }
                else -> {
                    dataDeleteAddressLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG deleteCase-> Conversion Error")
                }
            }
        }
    }

    private fun deleteCase(response: Response<Status>):
            Resource<Status> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->

                Timber.d("$TAG deleteCase->page->$status")
                status = resultResponse
                Timber.d("$TAG deleteCase->categories->$status")
                return Resource.Success(status ?: resultResponse)
            }
        }
        Timber.e("$TAG getCategories->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun deleteAddress(id:String) = viewModelScope.launch {
        deleteCase(
            share.getString(Constant.TOKEN,"").toString(), id
        )
    }



    fun requestPermission(context: Context) {
        Dexter.withContext(context)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(object : PermissionListener {

                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {

                        dataGetPrimistionLiveData.postValue(true)
                                }


                    override fun onPermissionRationaleShouldBeShown(
                            permission: PermissionRequest?,
                            token: PermissionToken?
                    ) {
                        token!!.continuePermissionRequest()
                        Log.e("eee","nust be show")
                        dataGetPrimistionLiveData.postValue(false)
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                        Toast.makeText(context,"No", Toast.LENGTH_SHORT).show()
                        dataGetPrimistionLiveData.postValue(false)
                    }
                }).check();
    }


    init {
       getAddress()
    }
    
    
}