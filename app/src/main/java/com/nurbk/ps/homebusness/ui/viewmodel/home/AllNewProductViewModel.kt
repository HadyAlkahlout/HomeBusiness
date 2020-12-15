package com.nurbk.ps.homebusness.ui.viewmodel.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nurbk.ps.homebusness.model.DataHome.DataAllNewProduct.DataNewProduct
import com.nurbk.ps.homebusness.repoistory.ApiRepository
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class AllNewProductViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "AllProductViewModel"

    private val repository = ApiRepository()


    val dataAllProductViewModel = MutableLiveData<Resource<DataNewProduct>>()
    var data: DataNewProduct? = null
    private val share by lazy {
        Constant.getSharePref(application.applicationContext)
    }
     var page = 1

    private suspend fun getAllProduct(
        Authorization: String, lang: String, keyword: String
    ) {
        dataAllProductViewModel.postValue(Resource.Loading())
        try {
            val response = repository
                .getDataAllHomeProduct(Authorization, lang, page.toString(), keyword)
            dataAllProductViewModel.postValue(getAllProduct(response))
            Timber.d("$TAG getAllProduct-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataAllProductViewModel.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getAllProduct->${t.message.toString()}")
                }
                else -> {
                    dataAllProductViewModel.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getAllProduct->${t.message.toString()}")
                }

            }
        }
    }

    private fun getAllProduct(response: Response<DataNewProduct>):
            Resource<DataNewProduct> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                page++
                Timber.d("$TAG getAllProduct->page->$data")
                if (data == null) {
                    data = resultResponse
                    Timber.d("$TAG getAllProduct->data->$data")
                } else {
                    val oldData = data
                    oldData!!.data.data.addAll(resultResponse.data.data)
                    Timber.d("$TAG getAllProduct->data->$oldData")

                }
                Timber.d("$TAG getAllProduct-> Resource.Success->$resultResponse")
                return Resource.Success(data ?: resultResponse)
            }
        }
        Timber.e("$TAG getAllProduct->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }

    fun getAllProduct(keyword: String) =
        viewModelScope.launch {
            getAllProduct(
                share.getString(Constant.TOKEN, "")!!,
                share.getString(Constant.LANG, "ar").toString(),
                keyword
            )
        }


}