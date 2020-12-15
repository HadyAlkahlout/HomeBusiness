package com.nurbk.ps.homebusness.ui.viewmodel.store

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nurbk.ps.homebusness.model.status.Status
import com.nurbk.ps.homebusness.repoistory.ApiRepository
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Constant.TOKEN
import com.nurbk.ps.homebusness.util.Constant.getSharePref
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class AddProductViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "AddCaseViewModel"

    private val repository = ApiRepository()

    val dataStatusLiveData = MutableLiveData<Resource<Status>>()
    private val share = getSharePref(application.applicationContext)

    private suspend fun addProduct(
        params: Map<String, RequestBody>,
        images: List<MultipartBody.Part>,
        type: Int
    ) {
        dataStatusLiveData.postValue(Resource.Loading())
        try {
            val response =
                if (type == 1)
                    repository
                        .addProduct(
                            share.getString(Constant.LANG, "ar").toString(),
                            share.getString(Constant.TOKEN, "").toString(),
                            params,
                            images
                        )
                else
                    repository
                        .updateProduct(
                            share.getString(Constant.LANG, "ar").toString(),
                            share.getString(Constant.TOKEN, "").toString(),
                            params,
                            images
                        )

            dataStatusLiveData.postValue(addProduct(response))
            Timber.d("$TAG uploadNewCase-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataStatusLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG uploadNewCase-> Network Failure")
                }
                else -> {
                    dataStatusLiveData.postValue(Resource.Error("Conversion Error"))
                    Timber.e("$TAG uploadNewCase-> Conversion Error")
                }

            }
        }
    }

    private fun addProduct(response: Response<Status>):
            Resource<Status> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG uploadNewCase->")
                Timber.d("$TAG uploadNewCase-> Resource.Success->$resultResponse")
                return Resource.Success(resultResponse)
            }
        }
        Timber.e("$TAG postBenefactor->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun addProducts(
        params: Map<String, RequestBody>,
        images: List<MultipartBody.Part>,
        i: Int
    ) =
        viewModelScope.launch {
            addProduct(
                params, images, i
            )
        }


    private suspend fun deleteImageProduct(
        id: String
    ) {
        dataStatusLiveData.postValue(Resource.Loading())
        try {
            val response =
                repository
                    .deleteImageProduct(
                        share.getString(Constant.LANG, "ar").toString(),
                        share.getString(TOKEN, "")!!, id
                    )

            dataStatusLiveData.postValue(addProduct(response))
            Timber.d("$TAG deleteCase-> OK")

        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataStatusLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG deleteCase-> ${t.message}")

                }
                else -> {
                    dataStatusLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG deleteCase-> ${t.message}")

                }
            }
        }
    }


    fun deleteImage(id: String) = viewModelScope.launch {
        deleteImageProduct(
            id
        )

    }

}