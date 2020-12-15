package com.nurbk.ps.homebusness.ui.viewmodel.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nurbk.ps.homebusness.model.DataHome.DataAllSpecialStore.DataAllSpecialStore
import com.nurbk.ps.homebusness.repoistory.ApiRepository
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class AllSpecialStoreViewModel(application: Application):AndroidViewModel(application) {


    private val TAG = "AllSpecialProductViewModel"

    private val repository = ApiRepository()


    val dataSpecialStoreViewModel = MutableLiveData<Resource<DataAllSpecialStore>>()
    var data: DataAllSpecialStore? = null
    private val share by lazy {
        Constant.getSharePref(application.applicationContext)
    }
    private var page = 1

    private suspend fun getDataSpecialStore(
        Authorization: String, lang: String
    ) {
        dataSpecialStoreViewModel.postValue(Resource.Loading())
        try {
            val response = repository
                .getDataSpecialStore(Authorization, lang, page.toString())
            dataSpecialStoreViewModel.postValue(getDataSpecialStore(response))
            Timber.d("$TAG getDataSpecialStore-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataSpecialStoreViewModel.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getDataSpecialStore->${t.message.toString()}")
                }
                else -> {
                    dataSpecialStoreViewModel.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getDataSpecialStore->${t.message.toString()}")
                }

            }
        }
    }

    private fun getDataSpecialStore(response: Response<DataAllSpecialStore>):
            Resource<DataAllSpecialStore> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                page++
                Timber.d("$TAG getDataSpecialStore->page->$data")
                if (data == null) {
                    data = resultResponse
                    Timber.d("$TAG getDataSpecialStore->data->$data")
                } else {
                    val oldData = data
                    oldData!!.data.data.addAll(resultResponse.data.data)
                    Timber.d("$TAG getDataSpecialStore->data->$oldData")

                }
                Timber.d("$TAG getDataSpecialStore-> Resource.Success->$resultResponse")
                return Resource.Success(data ?: resultResponse)
            }
        }
        Timber.e("$TAG getDataSpecialStore->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }

    fun getDataSpecialStore() =
        viewModelScope.launch {
            getDataSpecialStore(
                share.getString(Constant.TOKEN, "")!!,    share.getString(Constant.LANG, "ar").toString()
            )
        }




}