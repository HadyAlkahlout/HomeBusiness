package com.nurbk.ps.homebusness.ui.viewmodel.store

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nurbk.ps.homebusness.model.myorder.MyOrder
import com.nurbk.ps.homebusness.model.myproduct.ProductDetails
import com.nurbk.ps.homebusness.model.packages.Packages
import com.nurbk.ps.homebusness.model.packages.PostPackageID
import com.nurbk.ps.homebusness.model.packages.PostProductPackage
import com.nurbk.ps.homebusness.model.status.Status
import com.nurbk.ps.homebusness.repoistory.ApiRepository
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class PostUpgradeViewModel(application: Application) : AndroidViewModel(application) {

    val TAG = "PostDialogViewModel"

    private val repository = ApiRepository()

    val dataPostDialogLiveData = MutableLiveData<Resource<Status>>()
    private var status: Status? = null


    private val share by lazy {
        Constant.getSharePref(application.applicationContext)
    }

    private suspend fun upgradeAccount(
        Authorization: String,postPackageID: PostPackageID
    ) {
        dataPostDialogLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .upgradeaccount(postPackageID,Authorization, share.getString(Constant.LANG, "ar").toString())

            dataPostDialogLiveData.postValue(upgradeAccount(response))
            Timber.e("$TAG getMyOrder-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataPostDialogLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getMyOrder-> Network Failure")
                }
                else -> {
                    dataPostDialogLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getMyOrder-> Conversion Error")
                }

            }
        }
    }

    private fun upgradeAccount(response: Response<Status>):
            Resource<Status> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.e("$TAG getMyOrder->page->$status")
                status = resultResponse
                    Timber.d("$TAG getMyOrder->Favorite->$status")
                Timber.e("$TAG getMyOrder-> Resource.Success->$resultResponse")
                return Resource.Success(status ?: resultResponse)
            }
        }
        Timber.e("$TAG getMyOrder->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun upgradeAccount(postPackageID: PostPackageID) = viewModelScope.launch {
        upgradeAccount(
            share.getString(Constant.TOKEN,"").toString(),postPackageID
        )
    }


    //upgradeToSpecialAccount

    private suspend fun upgradeToSpecialAccount(
        Authorization: String,postPackageID: PostPackageID
    ) {
        dataPostDialogLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .upgradeToSpecialAccount(postPackageID,Authorization, share.getString(Constant.LANG, "ar").toString())

            dataPostDialogLiveData.postValue(upgradeToSpecialAccount(response))
            Timber.e("$TAG getMyOrder-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataPostDialogLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getMyOrder-> Network Failure")
                }
                else -> {
                    dataPostDialogLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getMyOrder-> Conversion Error")
                }

            }
        }
    }

    private fun upgradeToSpecialAccount(response: Response<Status>):
            Resource<Status> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.e("$TAG getMyOrder->page->$status")
                status = resultResponse
                Timber.d("$TAG getMyOrder->Favorite->$status")
                Timber.e("$TAG getMyOrder-> Resource.Success->$resultResponse")
                return Resource.Success(status ?: resultResponse)
            }
        }
        Timber.e("$TAG getMyOrder->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun upgradeToSpecialAccount(postPackageID: PostPackageID) = viewModelScope.launch {
        upgradeToSpecialAccount(
            share.getString(Constant.TOKEN,"").toString(),postPackageID
        )
    }


    //upgradeToSpecialAccount

    private suspend fun upgradeToSpecialProduct(
        Authorization: String,postProductPackage: PostProductPackage
    ) {
        dataPostDialogLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .upgradeToSpecialProduct(postProductPackage,Authorization, share.getString(Constant.LANG, "ar").toString())

            dataPostDialogLiveData.postValue(upgradeToSpecialProduct(response))
            Timber.e("$TAG getMyOrder-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataPostDialogLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getMyOrder-> Network Failure")
                }
                else -> {
                    dataPostDialogLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getMyOrder-> Conversion Error")
                }

            }
        }
    }

    private fun upgradeToSpecialProduct(response: Response<Status>):
            Resource<Status> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.e("$TAG getMyOrder->page->$status")
                status = resultResponse
                Timber.d("$TAG getMyOrder->Favorite->$status")
                Timber.e("$TAG getMyOrder-> Resource.Success->$resultResponse")
                return Resource.Success(status ?: resultResponse)
            }
        }
        Timber.e("$TAG getMyOrder->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun upgradeToSpecialProduct(postProductPackage: PostProductPackage) = viewModelScope.launch {
        upgradeToSpecialProduct(
            share.getString(Constant.TOKEN,"").toString(),postProductPackage
        )
    }



}