package com.nurbk.ps.homebusness.ui.viewmodel.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.nurbk.ps.homebusness.model.Country.Country
import com.nurbk.ps.homebusness.model.setting.Setting
import com.nurbk.ps.homebusness.repoistory.ApiRepository
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class SplashViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "SplashViewModel"

    val liveData: LiveData<SplashState>
        get() = mutableLiveData
    private val mutableLiveData = MutableLiveData<SplashState>()


    private val repository = ApiRepository()

    val dataStingLiveData = MutableLiveData<Resource<Setting>>()
    private var setting: Setting? = null
    private val share by lazy {
        Constant.getSharePref(application.applicationContext)
    }

    private suspend fun getSetting(
        Authorization: String
    ) {
        dataStingLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getSetting(Authorization, share.getString(Constant.LANG, "ar").toString())
            dataStingLiveData.postValue(getSetting(response))
            Timber.d("$TAG getSetting-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataStingLiveData.postValue(Resource.Error("Network Failure"))
                    Timber.e("$TAG getSetting-> Network Failure")
                }
                else -> {
                    dataStingLiveData.postValue(Resource.Error("Conversion Error"))
                    Timber.e("$TAG getSetting-> Conversion Error")
                }

            }
        }
    }

    private fun getSetting(response: Response<Setting>):
            Resource<Setting> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG getSetting->")
                setting = resultResponse
                Timber.d("$TAG getSetting->response->$setting")
                Timber.d("$TAG getSetting-> Resource.Success->$resultResponse")
                return Resource.Success(setting ?: resultResponse)
            }
        }
        Timber.e("$TAG getSetting->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    private fun getSetting() =
        viewModelScope.launch {
            getSetting(
                share.getString(Constant.TOKEN, "")!!
            )
        }


    val dataCityLiveData = MutableLiveData<Resource<Country>>()

    private suspend fun getCity(
        lang: String
    ) {
        dataCityLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getCountry(lang)
            dataCityLiveData.postValue(getCity(response))
            Timber.d("$TAG getCity-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataCityLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getCity->${t.message.toString()}")
                }
                else -> {
                    dataCityLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getCity->${t.message.toString()}")
                }

            }
        }
    }

    private fun getCity(response: Response<Country>):
            Resource<Country> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG getCity-> Resource.Success->$resultResponse")
                return Resource.Success(resultResponse).also {
                    GlobalScope.launch {
                        delay(2000)
                        mutableLiveData.postValue(SplashState.MainActivity)
                    }
                }
            }
        }
        Timber.e("$TAG getCity->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun getCity() =
        viewModelScope.launch {
            getCity(
                share.getString(Constant.LANG, "ar").toString()
            )
        }


    val dataFarRegionLiveData = MutableLiveData<Resource<Country>>()

    private suspend fun getFarRegions(
        lang: String
    ) {
        dataFarRegionLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getFarRegions(lang)
            dataFarRegionLiveData.postValue(getFarRegions(response))
            Timber.d("$TAG getCity-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataFarRegionLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getCity->${t.message.toString()}")
                }
                else -> {
                    dataFarRegionLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getCity->${t.message.toString()}")
                }

            }
        }
    }

    private fun getFarRegions(response: Response<Country>):
            Resource<Country> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG getFarRegions-> Resource.Success->$resultResponse")
                return Resource.Success(resultResponse)
            }
        }
        Timber.e("$TAG getFarRegions->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    private fun getRegion() =
        viewModelScope.launch {
            getFarRegions(
                share.getString(Constant.LANG, "ar").toString()
            )
        }


    init {
        getCity()
        getRegion()
        getSetting()
    }

}

sealed class SplashState {
    object MainActivity : SplashState()
}
