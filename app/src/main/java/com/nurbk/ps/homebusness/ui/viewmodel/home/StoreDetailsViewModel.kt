package com.nurbk.ps.homebusness.ui.viewmodel.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nurbk.ps.homebusness.model.ClassID
import com.nurbk.ps.homebusness.model.StoreDetails.StoreDetails
import com.nurbk.ps.homebusness.model.status.Status
import com.nurbk.ps.homebusness.repoistory.ApiRepository
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class StoreDetailsViewModel(application: Application) : AndroidViewModel(application) {


    private val TAG = "StoreDetailsViewModel"


    private val repository = ApiRepository()
    val dataStoreDetailsLiveData = MutableLiveData<Resource<StoreDetails>>()
    private val share by lazy {
        Constant.getSharePref(application.applicationContext)
    }

    private suspend fun getStoreDetails(
        Authorization: String, lang: String, id: String
    ) {
        dataStoreDetailsLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getStoreDetails(Authorization, lang, id)
            dataStoreDetailsLiveData.postValue(getStoreDetails(response))
            Timber.d("$TAG getStoreDetails-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataStoreDetailsLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getStoreDetails->${t.message.toString()}")
                }
                else -> {
                    dataStoreDetailsLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getStoreDetails->${t.message.toString()}")
                }

            }
        }
    }

    private fun getStoreDetails(response: Response<StoreDetails>):
            Resource<StoreDetails> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG getStoreDetails-> Resource.Success->$resultResponse")
                return Resource.Success(resultResponse)
            }
        }
        Timber.e("$TAG getStoreDetails->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun getStoreDetails(id: String) =
        viewModelScope.launch {
            getStoreDetails(
                share.getString(Constant.TOKEN, "")!!,
                share.getString(Constant.LANG, "ar").toString(),
                id
            )
        }


    val addRateLiveData = MutableLiveData<Resource<Status>>()


    private suspend fun addRate(
        params: Map<String, RequestBody>
    ) {
        addRateLiveData.postValue(Resource.Loading())
        try {
            val response =
                repository.addRate(
                    share.getString(Constant.TOKEN, "").toString(),
                    share.getString(Constant.LANG, "ar").toString(), params
                )

            addRateLiveData.postValue(addRate(response))
            Timber.d("$TAG addRate-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    addRateLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG addRate->${t.message.toString()}")
                }
                else -> {
                    addRateLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG addRate-> ${t.message.toString()}")
                }

            }
        }
    }


    private fun addRate(response: Response<Status>):
            Resource<Status> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.e("$TAG addRate->")
                Timber.e("$TAG addRate-> Resource.Success->$resultResponse")
                return Resource.Success(resultResponse)
            }
        }
        Timber.e("$TAG addRate->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun addRates(params: Map<String, RequestBody>) =
        viewModelScope.launch {
            addRate(
                params
            )
        }



    val addFollowLiveData = MutableLiveData<Resource<Status>>()


    private suspend fun addStoreFollow(
        id: ClassID
    ) {
        addFollowLiveData.postValue(Resource.Loading())
        try {
            val response =
                repository.addFollowStore(
                    share.getString(Constant.TOKEN, "").toString(),
                    share.getString(Constant.LANG, "ar").toString(), id
                )

            addFollowLiveData.postValue(addRate(response))
            Timber.d("$TAG addRate-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    addFollowLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG addRate->${t.message.toString()}")
                }
                else -> {
                    addFollowLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG addRate-> ${t.message.toString()}")
                }

            }
        }
    }
    fun addStoreFollow(id: String) =
        viewModelScope.launch {
            addStoreFollow(
                ClassID(id)
            )
        }

    val deleteFollowLiveData = MutableLiveData<Resource<Status>>()


    private suspend fun deleteFollowStore(
        id: ClassID
    ) {
        deleteFollowLiveData.postValue(Resource.Loading())
        try {
            val response =
                repository.deleteFollowStore(
                    share.getString(Constant.TOKEN, "").toString(),
                    share.getString(Constant.LANG, "ar").toString(), id
                )

            deleteFollowLiveData.postValue(addRate(response))
            Timber.d("$TAG addRate-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    deleteFollowLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG addRate->${t.message.toString()}")
                }
                else -> {
                    deleteFollowLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG addRate-> ${t.message.toString()}")
                }

            }
        }
    }

    fun deleteFollowStore(id: String) =
        viewModelScope.launch {
            deleteFollowStore(
                ClassID(id)
            )
        }

}