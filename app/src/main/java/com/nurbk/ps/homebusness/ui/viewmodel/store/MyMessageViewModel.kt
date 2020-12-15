package com.nurbk.ps.homebusness.ui.viewmodel.store

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nurbk.ps.homebusness.model.ClassID
import com.nurbk.ps.homebusness.model.cart.Cart
import com.nurbk.ps.homebusness.model.mymessage.Message
import com.nurbk.ps.homebusness.model.status.Status
import com.nurbk.ps.homebusness.repoistory.ApiRepository
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class MyMessageViewModel(application: Application) : AndroidViewModel(application) {

    val TAG = "MyMessageViewModel"

    private val repository = ApiRepository()

    var dataMyMessageLiveData = MutableLiveData<Resource<Message>>()
    private var message: Message? = null


    private val share by lazy {
        Constant.getSharePref(application.applicationContext)
    }

    private var page = 1


    private suspend fun getMyMessage(
        Authorization: String, lang: String
    ) {
        dataMyMessageLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getMyMessage(Authorization, page.toString(), lang)

            dataMyMessageLiveData.postValue(getMyMessage(response))
            Timber.e("$TAG getMyCart-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataMyMessageLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getMyCart-> Network Failure")
                }
                else -> {
                    dataMyMessageLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getMyCart-> Conversion Error")
                }

            }
        }
    }

    private fun getMyMessage(response: Response<Message>):
            Resource<Message> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                page++
                Timber.d("$TAG getDataSpecialProduct->page->$message")
                if (message == null) {
                    message = resultResponse
                    Timber.d("$TAG getDataSpecialProduct->data->$message")
                } else {
                    val oldData = message
                    oldData!!.data!!.data.addAll(resultResponse.data!!.data)
                    Timber.d("$TAG getDataSpecialProduct->data->$oldData")

                }
                Timber.d("$TAG getDataSpecialProduct-> Resource.Success->$resultResponse")
                return Resource.Success(message ?: resultResponse)
            }
        }
        Timber.e("$TAG getMyCart->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun getMyMessage() = viewModelScope.launch {
        getMyMessage(
            share.getString(Constant.TOKEN, "").toString(),
            share.getString(Constant.LANG, "ar").toString()
        )

        Log.e("eeee token", share.getString(Constant.TOKEN, "").toString())

    }




    init {
        getMyMessage()
    }
}