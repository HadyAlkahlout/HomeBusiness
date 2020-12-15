package com.nurbk.ps.homebusness.ui.viewmodel.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nurbk.ps.homebusness.model.ClassID
import com.nurbk.ps.homebusness.model.DataNotification.DataNotification
import com.nurbk.ps.homebusness.model.status.Status
import com.nurbk.ps.homebusness.repoistory.ApiRepository
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class NotificationViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "AllSpecialProductViewModel"

    private val repository = ApiRepository()


    val dataNotificationLiveData = MutableLiveData<Resource<DataNotification>>()
    var data: DataNotification? = null
    private val share by lazy {
        Constant.getSharePref(application.applicationContext)
    }
     var page = 1

    private suspend fun getNotification(
        Authorization: String, lang: String
    ) {
        dataNotificationLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getNotification(Authorization, lang, page.toString())
            dataNotificationLiveData.postValue(getNotification(response))
            Timber.d("$TAG getNotification-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataNotificationLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getNotification->${t.message.toString()}")
                }
                else -> {
                    dataNotificationLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getNotification->${t.message.toString()}")
                }

            }
        }
    }

    private fun getNotification(response: Response<DataNotification>):
            Resource<DataNotification> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                page++
                Timber.d("$TAG getNotification->page->$data")
                if (data == null) {
                    data = resultResponse
                    Timber.d("$TAG getNotification->data->$data")
                } else {
                    val oldData = data
                    oldData!!.data.data.addAll(resultResponse.data.data)
                    Timber.d("$TAG getNotification->data->$oldData")

                }
                Timber.d("$TAG getNotification-> Resource.Success->$resultResponse")
                return Resource.Success(data ?: resultResponse)
            }
        }
        Timber.e("$TAG getNotification->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }

    fun getNotification() =
        viewModelScope.launch {
            getNotification(
                "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEsImlzcyI6Imh0dHA6Ly9ob21lLm1lL2FwaS91c2VyL3JlZ2lzdGVyIiwiaWF0IjoxNjAwNzY2NDEwLCJleHAiOjE2MTEyNzg0MTAsIm5iZiI6MTYwMDc2NjQxMCwianRpIjoieXVtdUF4VllPaHozcHZKMiJ9.EzocLjsDlEMS9pMfgDvbLsCKCCe8uFlRyPHvdcZdVt0",
                share.getString(Constant.LANG, "ar").toString()
            )
        }




    val statusLiveData = MutableLiveData<Resource<Status>>()


    private suspend fun readNotification(
        Authorization: String,
        id: String
    ) {
        statusLiveData.postValue(Resource.Loading())
        try {
            val response =
                repository.readNotification(
                    Authorization,
                    share.getString(Constant.LANG, "ar").toString(),
                    id
                )

            statusLiveData.postValue(status(response))
            Timber.d("$TAG status-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    statusLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG status->${t.message.toString()}")
                }
                else -> {
                    statusLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG status-> ${t.message.toString()}")
                }

            }
        }
    }


    private fun status(response: Response<Status>):
            Resource<Status> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.e("$TAG status->")
                Timber.e("$TAG status-> Resource.Success->$resultResponse")
                return Resource.Success(resultResponse)
            }
        }
        Timber.e("$TAG status->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun readNotification(id: String) =
        viewModelScope.launch {
            readNotification(
                "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEsImlzcyI6Imh0dHA6Ly9ob21lLm1lL2FwaS91c2VyL3JlZ2lzdGVyIiwiaWF0IjoxNjAwNzY2NDEwLCJleHAiOjE2MTEyNzg0MTAsIm5iZiI6MTYwMDc2NjQxMCwianRpIjoieXVtdUF4VllPaHozcHZKMiJ9.EzocLjsDlEMS9pMfgDvbLsCKCCe8uFlRyPHvdcZdVt0",
                id
            )
        }
}