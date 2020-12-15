package com.nurbk.ps.homebusness.ui.viewmodel.category

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nurbk.ps.homebusness.model.DataCategories.StoreCategory.StoreCategory
import com.nurbk.ps.homebusness.model.DataHome.DataAllNewStore.DataNewStore
import com.nurbk.ps.homebusness.repoistory.ApiRepository
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class StoreCategoryViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "StoreCategoryViewModel"

    private val repository = ApiRepository()

    private val share by lazy {
        Constant.getSharePref(application.applicationContext)
    }
    val dataStoreCategoryViewModel = MutableLiveData<Resource<StoreCategory>>()
    var data: StoreCategory? = null

    private var page = 1

    private suspend fun getDataStoreCategory(
        Authorization: String, lang: String, catId: String
    ) {
        dataStoreCategoryViewModel.postValue(Resource.Loading())
        try {
            val response = repository
                .getDataStoreCategory(Authorization, lang, page.toString(), catId)
            dataStoreCategoryViewModel.postValue(getDataStoreCategory(response))
            Timber.d("$TAG getDataStoreCategory-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataStoreCategoryViewModel.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getDataStoreCategory->${t.message.toString()}")
                }
                else -> {
                    dataStoreCategoryViewModel.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getDataStoreCategory->${t.message.toString()}")
                }

            }
        }
    }

    private fun getDataStoreCategory(response: Response<StoreCategory>):
            Resource<StoreCategory> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                page++
                Timber.d("$TAG getDataStoreCategory->page->$data")
                if (data == null) {
                    data = resultResponse
                    Timber.d("$TAG getDataStoreCategory->data->$data")
                } else {
                    val oldData = data
                    oldData!!.data.data.addAll(resultResponse.data.data)
                    Timber.d("$TAG getDataStoreCategory->data->$oldData")

                }
                Timber.d("$TAG getDataStoreCategory-> Resource.Success->$resultResponse")
                return Resource.Success(data ?: resultResponse)
            }
        }
        Timber.e("$TAG getDataStoreCategory->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }

    fun getDataStoreCategory(catId: String) =
        viewModelScope.launch {
            getDataStoreCategory(
                share.getString(Constant.TOKEN, "")!!,    share.getString(Constant.LANG, "ar").toString()
                , catId
            )
        }



}