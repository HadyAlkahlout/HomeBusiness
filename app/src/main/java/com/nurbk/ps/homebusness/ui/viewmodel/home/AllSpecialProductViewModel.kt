package com.nurbk.ps.homebusness.ui.viewmodel.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nurbk.ps.homebusness.model.DataHome.DataAllSpecialProduct.DataAllSpecialProduct
import com.nurbk.ps.homebusness.repoistory.ApiRepository
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class AllSpecialProductViewModel(application: Application):AndroidViewModel(application) {


    private val TAG = "AllSpecialProductViewModel"

    private val repository = ApiRepository()


    val dataSpecialProductViewModel = MutableLiveData<Resource<DataAllSpecialProduct>>()
    var data: DataAllSpecialProduct? = null
    private val share by lazy {
        Constant.getSharePref(application.applicationContext)
    }
    private var page = 1

    private suspend fun getDataSpecialProduct(
        Authorization: String, lang: String
    ) {
        dataSpecialProductViewModel.postValue(Resource.Loading())
        try {
            val response = repository
                .getDataSpecialProduct(Authorization, lang, page.toString())
            dataSpecialProductViewModel.postValue(getDataSpecialProduct(response))
            Timber.d("$TAG getDataSpecialProduct-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataSpecialProductViewModel.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getDataSpecialProduct->${t.message.toString()}")
                }
                else -> {
                    dataSpecialProductViewModel.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getDataSpecialProduct->${t.message.toString()}")
                }

            }
        }
    }

    private fun getDataSpecialProduct(response: Response<DataAllSpecialProduct>):
            Resource<DataAllSpecialProduct> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                page++
                Timber.d("$TAG getDataSpecialProduct->page->$data")
                if (data == null) {
                    data = resultResponse
                    Timber.d("$TAG getDataSpecialProduct->data->$data")
                } else {
                    val oldData = data
                    oldData!!.data.data.addAll(resultResponse.data.data)
                    Timber.d("$TAG getDataSpecialProduct->data->$oldData")

                }
                Timber.d("$TAG getDataSpecialProduct-> Resource.Success->$resultResponse")
                return Resource.Success(data ?: resultResponse)
            }
        }
        Timber.e("$TAG getDataSpecialProduct->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }

    fun getDataSpecialProduct() =
        viewModelScope.launch {
            getDataSpecialProduct(
                share.getString(Constant.TOKEN, "")!!,    share.getString(Constant.LANG, "ar").toString()
            )
        }




}