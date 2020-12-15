package com.nurbk.ps.homebusness.ui.viewmodel.category

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nurbk.ps.homebusness.model.DataCategories.DataCategoires
import com.nurbk.ps.homebusness.repoistory.ApiRepository
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class CategoriesViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "CategoriesViewModel"

    private val repository = ApiRepository()

    private val share by lazy {
        Constant.getSharePref(application.applicationContext)
    }
    val dataCategoriesLiveData = MutableLiveData<Resource<DataCategoires>>()

    private suspend fun getDataCategories(
        Authorization: String, lang: String
    ) {
        dataCategoriesLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getCategories(Authorization, lang)
            dataCategoriesLiveData.postValue(getDataCategories(response))
            Timber.d("$TAG getDataCategories-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataCategoriesLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getDataCategories->${t.message.toString()}")
                }
                else -> {
                    dataCategoriesLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getDataCategories->${t.message.toString()}")
                }

            }
        }
    }

    private fun getDataCategories(response: Response<DataCategoires>):
            Resource<DataCategoires> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG getDataCategories-> Resource.Success->$resultResponse")
                return Resource.Success(resultResponse)
            }
        }
        Timber.e("$TAG getDataCategories->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    private fun getDataCategories() =
        viewModelScope.launch {
            getDataCategories(
                share.getString(Constant.TOKEN, "")!!,
                share.getString(Constant.LANG, "ar").toString()

            )
        }

    init {
        getDataCategories()
    }

}


