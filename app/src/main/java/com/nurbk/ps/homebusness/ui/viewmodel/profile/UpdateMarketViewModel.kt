package com.nurbk.ps.homebusness.ui.viewmodel.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nurbk.ps.homebusness.model.DataCategories.DataCategoires
import com.nurbk.ps.homebusness.model.delivery.DelevieryData
import com.nurbk.ps.homebusness.model.profile.Profile
import com.nurbk.ps.homebusness.repoistory.ApiRepository
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class UpdateMarketViewModel(application: Application) : AndroidViewModel(application) {

    val TAG = "UpdateMarketViewModel"

    private val repository = ApiRepository()

    val dataMarketLiveData = MutableLiveData<Resource<Profile>>()


    private val share by lazy {
        Constant.getSharePref(application.applicationContext)
    }


    private suspend fun getDataMarket(
        params: Map<String, RequestBody>,
        images: MultipartBody.Part
    ) {
        dataMarketLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .updateAccount(

                    share.getString(Constant.TOKEN, "").toString(),
                    share.getString(Constant.LANG, "ar").toString(),
                    params,
                    images
                )

            dataMarketLiveData.postValue(getDataMarket(response))
            Timber.e("$TAG getDataMarket-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataMarketLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getDataMarket-> Network Failure")
                }
                else -> {
                    dataMarketLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getDataMarket-> Conversion Error")
                }

            }
        }
    }

    private fun getDataMarket(response: Response<Profile>):
            Resource<Profile> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.e("$TAG getDataMarket-> Resource.Success->$resultResponse")
                return Resource.Success(resultResponse)
            }
        }
        Timber.e("$TAG getDataMarket->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun getDataMarkets(
        params: Map<String, RequestBody>,
        images: MultipartBody.Part
    ) = viewModelScope.launch {
        getDataMarket(
            params,
            images
        )
    }


    val dataSubCategoryLiveData = MutableLiveData<Resource<DataCategoires>>()


    private suspend fun getSubCategory(
        id: String
    ) {
        dataSubCategoryLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getSubCategory(
                    share.getString(Constant.TOKEN, "")!!,
                    share.getString(Constant.LANG, "ar").toString(),
                    id
                )

            dataSubCategoryLiveData.postValue(getSubCategory(response))
            Timber.e("$TAG getSubCategory-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataSubCategoryLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getSubCategory-> Network Failure")
                }
                else -> {
                    dataSubCategoryLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getSubCategory-> Conversion Error")
                }

            }
        }
    }

    private fun getSubCategory(response: Response<DataCategoires>):
            Resource<DataCategoires> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.e("$TAG getSubCategory-> Resource.Success->$resultResponse")
                return Resource.Success(resultResponse)
            }
        }
        Timber.e("$TAG getSubCategory->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun getSubCategories(
        id: String
    ) = viewModelScope.launch {
        getSubCategory(
            id
        )
    }


    val dataDeliveryLiveData = MutableLiveData<Resource<DelevieryData>>()


    private suspend fun getDeliveryTime(
    ) {
        dataDeliveryLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getDeliveryTime(
                    share.getString(Constant.TOKEN, "").toString(),
                    share.getString(Constant.LANG, "ar").toString()

                )

            dataDeliveryLiveData.postValue(getDeliveryTime(response))
            Timber.e("$TAG getDeliveryTime-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataDeliveryLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getDeliveryTime-> Network Failure")
                }
                else -> {
                    dataDeliveryLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getDeliveryTime-> Conversion Error")
                }

            }
        }
    }

    private fun getDeliveryTime(response: Response<DelevieryData>):
            Resource<DelevieryData> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.e("$TAG getDeliveryTime-> Resource.Success->$resultResponse")
                return Resource.Success(resultResponse)
            }
        }
        Timber.e("$TAG getDeliveryTime->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun getDeliveryTimes(
    ) = viewModelScope.launch {
        getDeliveryTime(
        )
    }
}