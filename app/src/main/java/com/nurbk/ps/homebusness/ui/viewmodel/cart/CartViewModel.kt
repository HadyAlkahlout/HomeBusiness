package com.nurbk.ps.homebusness.ui.viewmodel.cart

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nurbk.ps.homebusness.model.ClassID
import com.nurbk.ps.homebusness.model.cart.Cart
import com.nurbk.ps.homebusness.model.status.Status
import com.nurbk.ps.homebusness.repoistory.ApiRepository
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class CartViewModel(application: Application) : AndroidViewModel(application) {

    val TAG = "CartViewModel"

    private val repository = ApiRepository()

    var dataCartLiveData = MutableLiveData<Resource<Cart>>()
    val dataDeleteLiveData = MutableLiveData<Resource<Status>>()
    private var cart: Cart? = null


    private val share by lazy {
        Constant.getSharePref(application.applicationContext)
    }

    private var page = 1


    private suspend fun getMyCart(
        Authorization: String, lang: String
    ) {
        dataCartLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getMyCart(Authorization, page.toString(), lang)

            dataCartLiveData.postValue(getMyCart(response))
            Timber.e("$TAG getMyCart-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataCartLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getMyCart-> Network Failure")
                }
                else -> {
                    dataCartLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getMyCart-> Conversion Error")
                }

            }
        }
    }

    private fun getMyCart(response: Response<Cart>):
            Resource<Cart> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->

                    Timber.d("$TAG getMyCart->Favorite->$cart")
                Timber.e("$TAG getMyCart-> Resource.Success->$resultResponse")
                return Resource.Success(resultResponse)
            }
        }
        Timber.e("$TAG getMyCart->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun getMyCart() = viewModelScope.launch {
        getMyCart(
            share.getString(Constant.TOKEN, "").toString(),
            share.getString(Constant.LANG, "ar").toString()
        )

        Log.e("eeee token", share.getString(Constant.TOKEN, "").toString())

    }



    private suspend fun deleteOrder(
            classID: ClassID
    ) {
        dataDeleteLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                    .deleteOrder(classID,share.getString(Constant.TOKEN, "").toString(),
                            share.getString(Constant.LANG, "ar").toString())

            dataDeleteLiveData.postValue(deleteOrder(response))
            Timber.e("$TAG getMyCart-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataDeleteLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getMyCart-> Network Failure")
                }
                else -> {
                    dataDeleteLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getMyCart-> Conversion Error")
                }

            }
        }
    }

    private fun deleteOrder(response: Response<Status>):
            Resource<Status> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->

                Timber.d("$TAG getMyCart->Favorite->$cart")
                Timber.e("$TAG getMyCart-> Resource.Success->$resultResponse")
                return Resource.Success(resultResponse)
            }
        }
        Timber.e("$TAG getMyCart->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun deleteOrders(classID: ClassID) = viewModelScope.launch {
        deleteOrder(
                classID
        )


    }





    init {
        getMyCart()
    }
}