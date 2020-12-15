package com.nurbk.ps.homebusness.ui.viewmodel.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nurbk.ps.homebusness.model.myorder.MyOrder
import com.nurbk.ps.homebusness.repoistory.ApiRepository
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class MyOrderViewModel(application: Application) : AndroidViewModel(application) {

    val TAG = "myOrderViewModel"

    private val repository = ApiRepository()

    val dataMyOrderLiveData = MutableLiveData<Resource<MyOrder>>()
    private var myorder: MyOrder? = null


    private var page = 1
    private val share by lazy {
        Constant.getSharePref(application.applicationContext)
    }

    private suspend fun getMyOrder(
        Authorization: String
    ) {
        dataMyOrderLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getOrder(Authorization, page.toString(), share.getString(Constant.LANG, "ar").toString())

            dataMyOrderLiveData.postValue(getMyOrder(response))
            Timber.e("$TAG getMyOrder-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataMyOrderLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getMyOrder-> Network Failure")
                }
                else -> {
                    dataMyOrderLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getMyOrder-> Conversion Error")
                }

            }
        }
    }

    private fun getMyOrder(response: Response<MyOrder>):
            Resource<MyOrder> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                page++
                Timber.e("$TAG getMyOrder->page->$myorder")
                if (myorder == null) {
                    myorder = resultResponse
                    Timber.d("$TAG getMyOrder->Favorite->$myorder")
                } else {
                    val oldImage = myorder
                    oldImage!!.data.data.addAll(resultResponse.data.data)
                    Timber.e("$TAG getMyOrder->getFavorite->$oldImage")

                }
                Timber.e("$TAG getMyOrder-> Resource.Success->$resultResponse")
                return Resource.Success(myorder ?: resultResponse)
            }
        }
        Timber.e("$TAG getMyOrder->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun getMyOrder() = viewModelScope.launch {
        getMyOrder(
            share.getString(Constant.TOKEN, "").toString()        )
    }


    init {
        getMyOrder()
    }
}