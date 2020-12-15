package com.nurbk.ps.homebusness.ui.viewmodel.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nurbk.ps.homebusness.model.DataHome.DataAllNewStore.DataNewStore
import com.nurbk.ps.homebusness.repoistory.ApiRepository
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class AllNewStoreViewModel(application: Application):AndroidViewModel(application){

    private val TAG = "AllNewStoreViewModel"

    private val repository = ApiRepository()


    val dataNewStoreViewModel = MutableLiveData<Resource<DataNewStore>>()
    var data: DataNewStore? = null
    private val share by lazy {
        Constant.getSharePref(application.applicationContext)
    }
    private var page = 1

    private suspend fun getDataStore(
        Authorization: String, lang: String
    ) {
        dataNewStoreViewModel.postValue(Resource.Loading())
        try {
            val response = repository
                .getDataNewStore(Authorization, lang, page.toString())
            dataNewStoreViewModel.postValue(getDataStore(response))
            Timber.d("$TAG getDataStore-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataNewStoreViewModel.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getDataStore->${t.message.toString()}")
                }
                else -> {
                    dataNewStoreViewModel.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getDataStore->${t.message.toString()}")
                }

            }
        }
    }

    private fun getDataStore(response: Response<DataNewStore>):
            Resource<DataNewStore> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                page++
                Timber.d("$TAG getDataStore->page->$data")
                if (data == null) {
                    data = resultResponse
                    Timber.d("$TAG getDataStore->data->$data")
                } else {
                    val oldData = data
                    oldData!!.data.data.addAll(resultResponse.data.data)
                    Timber.d("$TAG getDataStore->data->$oldData")

                }
                Timber.d("$TAG getDataStore-> Resource.Success->$resultResponse")
                return Resource.Success(data ?: resultResponse)
            }
        }
        Timber.e("$TAG getDataStore->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }

    fun getDataStore() =
        viewModelScope.launch {
            getDataStore(
                share.getString(Constant.TOKEN, "")!!,    share.getString(Constant.LANG, "ar").toString()
            )
        }



}