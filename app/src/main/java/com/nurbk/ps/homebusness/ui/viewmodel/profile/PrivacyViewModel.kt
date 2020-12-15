package com.nurbk.ps.homebusness.ui.viewmodel.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nurbk.ps.homebusness.model.conditions.Conditions
import com.nurbk.ps.homebusness.model.privacy.Privacy
import com.nurbk.ps.homebusness.repoistory.ApiRepository
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class PrivacyViewModel(application: Application) :
    AndroidViewModel(application) {

    private val TAG = "PrivacyViewModel"
    private val TAG2 = "getCondation"

    private val repository = ApiRepository()

    //privecy
    val dataPrivacyLiveData = MutableLiveData<Resource<Privacy>>()
    private var privacy: Privacy? = null
    private val share by lazy {
        Constant.getSharePref(application.applicationContext)
    }
    //Conditoin
    val dataConditionsLiveData =
        MutableLiveData<Resource<Conditions>>()
    private var conditions: Conditions? = null


    private suspend fun getPrivacy(
        Authorization: String
    ) {
        dataPrivacyLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getPrivacy(Authorization, share.getString(Constant.LANG, "ar").toString())
            dataPrivacyLiveData.postValue(getPrivacy(response))
            Timber.d("$TAG getPrivacy-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataPrivacyLiveData.postValue(Resource.Error("Network Failure"))
                    Timber.e("$TAG getPrivacy-> Network Failure")
                }
                else -> {
                    dataPrivacyLiveData.postValue(Resource.Error("Conversion Error"))
                    Timber.e("$TAG getPrivacy-> Conversion Error")
                }

            }
        }
    }

    private fun getPrivacy(response: Response<Privacy>):
          Resource<Privacy> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG getPrivacy->")
                privacy = resultResponse
                Timber.d("$TAG getPrivacy->response->$privacy")
                Timber.d("$TAG getPrivacy-> Resource.Success->$resultResponse")
                return Resource.Success(privacy ?: resultResponse)
            }
        }
        Timber.e("$TAG getPrivacy->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun getPrivacy() =
        viewModelScope.launch {
            getPrivacy(
                share.getString(Constant.TOKEN, "").toString()
            )
        }


    private suspend fun getConditions(
        Authorization: String
    ) {
        dataConditionsLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getConditions(Authorization, share.getString(Constant.LANG, "ar").toString())

            dataConditionsLiveData.postValue(getConditions(response))
            Timber.d("$TAG2 getAllCategories-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataConditionsLiveData.postValue(Resource.Error("Network Failure"))
                    Timber.e("$TAG2 getAllCategories-> Network Failure")
                }
                else -> {
                    dataConditionsLiveData.postValue(Resource.Error("Conversion Error"))
                    Timber.e("$TAG2 getAllCategories-> Conversion Error")
                }

            }
        }
    }

    private fun getConditions(response: Response<Conditions>):
         Resource<Conditions> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG2 getAllCategories->")
                conditions = resultResponse
                Timber.d("$TAG2 getAllCategories->response->$conditions")
                Timber.d("$TAG2 getAllCategories-> Resource.Success->$resultResponse")
                return Resource.Success(conditions ?: resultResponse)
            }
        }
        Timber.e("$TAG2 getAllCategories->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun getConditions() =
        viewModelScope.launch {
            getConditions(
                share.getString(Constant.TOKEN, "").toString()
            )
        }

}