package com.nurbk.ps.homebusness.ui.viewmodel.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nurbk.ps.homebusness.model.DataHome.DataHome
import com.nurbk.ps.homebusness.repoistory.ApiRepository
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "HomeViewModel"


    private val repository = ApiRepository()
    val dataHomeLiveData = MutableLiveData<Resource<DataHome>>()
    private val share by lazy {
        Constant.getSharePref(application.applicationContext)
    }
    private suspend fun getDataHome(
        Authorization: String, lang: String
    ) {
        dataHomeLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getDataHome(Authorization, lang)
            dataHomeLiveData.postValue(getDataHome(response))
            Timber.d("$TAG getDataHome-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataHomeLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getDataHome->${t.message.toString()}")
                }
                else -> {
                    dataHomeLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getDataHome->${t.message.toString()}")
                }

            }
        }
    }

    private fun getDataHome(response: Response<DataHome>):
            Resource<DataHome> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG getDataHome-> Resource.Success->$resultResponse")
                return Resource.Success(resultResponse)
            }
        }
        Timber.e("$TAG getDataHome->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    private fun getDataHome() =
        viewModelScope.launch {
            getDataHome(
                share.getString(Constant.TOKEN, "")!!,    share.getString(Constant.LANG, "ar").toString()
            )
            Log.e("Eeee tokken",share.getString(Constant.TOKEN, "")!!.toString())
        }

    init {
        getDataHome()
    }



}