package com.nurbk.ps.homebusness.ui.viewmodel.store

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nurbk.ps.homebusness.model.myorder.MyOrder
import com.nurbk.ps.homebusness.model.myproduct.ProductDetails
import com.nurbk.ps.homebusness.model.packages.Packages
import com.nurbk.ps.homebusness.model.status.Status
import com.nurbk.ps.homebusness.repoistory.ApiRepository
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class DialogViewModel(application: Application) : AndroidViewModel(application) {

    val TAG = "DialogViewModel"

    private val repository = ApiRepository()

    val dataMyDialogLiveData = MutableLiveData<Resource<Packages>>()
    private var packages: Packages? = null


    private val share by lazy {
        Constant.getSharePref(application.applicationContext)
    }

    private suspend fun getSubscribe(
        Authorization: String
    ) {
        dataMyDialogLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getSubscribe(Authorization, share.getString(Constant.LANG, "ar").toString())

            dataMyDialogLiveData.postValue(getSubscribe(response))
            Timber.e("$TAG getMyOrder-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataMyDialogLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getMyOrder-> Network Failure")
                }
                else -> {
                    dataMyDialogLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getMyOrder-> Conversion Error")
                }

            }
        }
    }

    private fun getSubscribe(response: Response<Packages>):
            Resource<Packages> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.e("$TAG getMyOrder->page->$packages")
                packages = resultResponse
                    Timber.d("$TAG getMyOrder->Favorite->$packages")
                Timber.e("$TAG getMyOrder-> Resource.Success->$resultResponse")
                return Resource.Success(packages ?: resultResponse)
            }
        }
        Timber.e("$TAG getMyOrder->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun getSubscribe() = viewModelScope.launch {
        getSubscribe(
            share.getString(Constant.TOKEN,"").toString()
        )
    }



    ///// getTameezProduct

    private suspend fun getTameezProduct(
        Authorization: String
    ) {
        dataMyDialogLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getTameezproduct(Authorization, share.getString(Constant.LANG, "ar").toString())

            dataMyDialogLiveData.postValue(getTameezProduct(response))
            Timber.e("$TAG getMyOrder-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataMyDialogLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getMyOrder-> Network Failure")
                }
                else -> {
                    dataMyDialogLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getMyOrder-> Conversion Error")
                }

            }
        }
    }

    private fun getTameezProduct(response: Response<Packages>):
            Resource<Packages> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.e("$TAG getMyOrder->page->$packages")
                packages = resultResponse
                Timber.d("$TAG getMyOrder->Favorite->$packages")
                Timber.e("$TAG getMyOrder-> Resource.Success->$resultResponse")
                return Resource.Success(packages ?: resultResponse)
            }
        }
        Timber.e("$TAG getMyOrder->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun getTameezProduct() = viewModelScope.launch {
        getTameezProduct(
            share.getString(Constant.TOKEN,"").toString()
        )
    }


    ///// getTameezStore

    private suspend fun getTameezStore(
        Authorization: String
    ) {
        dataMyDialogLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getTameezStore(Authorization, share.getString(Constant.LANG, "ar").toString())

            dataMyDialogLiveData.postValue(getTameezStore(response))
            Timber.e("$TAG getMyOrder-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataMyDialogLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getMyOrder-> Network Failure")
                }
                else -> {
                    dataMyDialogLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getMyOrder-> Conversion Error")
                }

            }
        }
    }

    private fun getTameezStore(response: Response<Packages>):
            Resource<Packages> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.e("$TAG getMyOrder->page->$packages")
                packages = resultResponse
                Timber.d("$TAG getMyOrder->Favorite->$packages")
                Timber.e("$TAG getMyOrder-> Resource.Success->$resultResponse")
                return Resource.Success(packages ?: resultResponse)
            }
        }
        Timber.e("$TAG getMyOrder->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun getTameezStore() = viewModelScope.launch {
        getTameezStore(
            share.getString(Constant.TOKEN,"").toString()
        )
    }




}