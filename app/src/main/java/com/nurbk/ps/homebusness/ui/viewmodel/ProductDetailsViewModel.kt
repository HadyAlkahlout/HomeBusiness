package com.nurbk.ps.homebusness.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nurbk.ps.homebusness.model.ClassID
import com.nurbk.ps.homebusness.model.ProductDetails.ProductDetails
import com.nurbk.ps.homebusness.model.status.Status
import com.nurbk.ps.homebusness.repoistory.ApiRepository
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class ProductDetailsViewModel(application: Application) : AndroidViewModel(application) {


    private val TAG = "ProductDetailsViewModel"

    private val repository = ApiRepository()
    val dataProductDetailsLiveData = MutableLiveData<Resource<ProductDetails>>()
    private val share by lazy {
        Constant.getSharePref(application.applicationContext)
    }

    private suspend fun getProductDetails(
        Authorization: String, lang: String, id: String
    ) {
        dataProductDetailsLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getProductDetails(Authorization, lang, id)
            dataProductDetailsLiveData.postValue(getProductDetails(response))
            Timber.d("$TAG getProductDetails-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataProductDetailsLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getProductDetails->${t.message.toString()}")
                }
                else -> {
                    dataProductDetailsLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getProductDetails->${t.message.toString()}")
                }

            }
        }
    }

    private fun getProductDetails(response: Response<ProductDetails>):
            Resource<ProductDetails> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG getProductDetails-> Resource.Success->$resultResponse")
                return Resource.Success(resultResponse)
            }
        }
        Timber.e("$TAG getProductDetails->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun getProductDetails(id: String) =
        viewModelScope.launch {
            getProductDetails(
                share.getString(Constant.TOKEN, "").toString(),
                share.getString(Constant.LANG, "ar").toString(),
                id
            )
        }


    val statusLiveData = MutableLiveData<Resource<Status>>()


    private suspend fun addFav(
        Authorization: String,
        id: ClassID
    ) {
        statusLiveData.postValue(Resource.Loading())
        try {
            val response =
                repository.addFav(
                    Authorization,
                    share.getString(Constant.LANG, "ar").toString(),
                    id
                )

            statusLiveData.postValue(status(response))
            Timber.d("$TAG addFav-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    statusLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG addFav->${t.message.toString()}")
                }
                else -> {
                    statusLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG addFav-> ${t.message.toString()}")
                }

            }
        }
    }

    val deleteLiveData = MutableLiveData<Resource<Status>>()

    private suspend fun deleteFav(
        Authorization: String,
        id: ClassID
    ) {
        deleteLiveData.postValue(Resource.Loading())
        try {
            val response =
                repository.deleteFav(
                    Authorization,
                    share.getString(Constant.LANG, "ar").toString(),
                    id
                )

            deleteLiveData.postValue(status(response))
            Timber.d("$TAG addFav-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    deleteLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG addFav->${t.message.toString()}")
                }
                else -> {
                    deleteLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG addFav-> ${t.message.toString()}")
                }

            }
        }
    }

    private fun status(response: Response<Status>):
            Resource<Status> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.e("$TAG addFav->")
                Timber.e("$TAG addFav-> Resource.Success->$resultResponse")
                return Resource.Success(resultResponse)
            }
        }
        Timber.e("$TAG addFav->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun addFav(id: ClassID) =
        viewModelScope.launch {
            addFav(
                share.getString(Constant.TOKEN, "").toString(),
                id
            )
        }

    fun deleteFav(id: ClassID) =
        viewModelScope.launch {
            deleteFav(
                share.getString(Constant.TOKEN, "").toString(),
                id
            )
        }

    val addCartLiveData = MutableLiveData<Resource<Status>>()


    private suspend fun addCart(
        params: Map<String, RequestBody>
    ) {
        addCartLiveData.postValue(Resource.Loading())
        try {
            val response =
                repository.addToCart(
                    share.getString(Constant.TOKEN, "").toString(),
                    share.getString(Constant.LANG, "ar").toString(), params
                )

            addCartLiveData.postValue(status(response))
            Timber.d("$TAG addCart-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    addCartLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG addCart->${t.message.toString()}")
                }
                else -> {
                    addCartLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG addCart-> ${t.message.toString()}")
                }

            }
        }
    }


    fun addCarts(params: Map<String, RequestBody>) =
        viewModelScope.launch {
            addCart(
                params
            )
        }
}