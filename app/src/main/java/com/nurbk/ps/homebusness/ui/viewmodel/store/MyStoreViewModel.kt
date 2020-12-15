package com.nurbk.ps.homebusness.ui.viewmodel.store

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nurbk.ps.homebusness.model.myorder.MyOrder
import com.nurbk.ps.homebusness.model.myproduct.ProductDetails
import com.nurbk.ps.homebusness.model.myproduct.UpdateStatus
import com.nurbk.ps.homebusness.model.status.Status
import com.nurbk.ps.homebusness.repoistory.ApiRepository
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class MyStoreViewModel(application: Application) : AndroidViewModel(application) {

    val TAG = "myOrderViewModel"

    private val repository = ApiRepository()

    val dataMyOrderLiveData = MutableLiveData<Resource<MyOrder>>()
    val dataChangeStatusLiveData = MutableLiveData<Resource<Status>>()
    val dataMyProductLiveData = MutableLiveData<Resource<ProductDetails>>()
    private var myorder: MyOrder? = null
    private var status: ProductDetails? = null
    private var product_status: Status? = null


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
                .getMyOrder(
                    Authorization,
                    page.toString(),
                    share.getString(Constant.LANG, "ar").toString()
                )

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
            share.getString(Constant.TOKEN, "").toString()
        )
    }


    private suspend fun getMyProduct(
        Authorization: String
    ) {
        dataMyProductLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getMyProduct(Authorization, share.getString(Constant.LANG, "ar").toString())

            dataMyProductLiveData.postValue(getMyProduct(response))
            Timber.e("$TAG getMyOrder-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataMyProductLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getMyOrder-> Network Failure")
                }
                else -> {
                    dataMyProductLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getMyOrder-> Conversion Error")
                }

            }
        }
    }

    private fun getMyProduct(response: Response<ProductDetails>):
            Resource<ProductDetails> {
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


    fun getMyProduct() = viewModelScope.launch {
        getMyProduct(
            share.getString(Constant.TOKEN, "").toString()
        )
    }


    ///// Change Product status

    private suspend fun ChangeProductStatus(
        Authorization: String, updateStatus: UpdateStatus
    ) {
        dataChangeStatusLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .UpdateProductStatus(
                    updateStatus,
                    Authorization,
                    share.getString(Constant.LANG, "ar").toString()
                )

            dataChangeStatusLiveData.postValue(ChangeProductStatus(response))
            Timber.e("$TAG getMyOrder-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataChangeStatusLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getMyOrder-> Network Failure")
                }
                else -> {
                    dataChangeStatusLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getMyOrder-> Conversion Error")
                }

            }
        }
    }

    private fun ChangeProductStatus(response: Response<Status>):
            Resource<Status> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.e("$TAG getMyOrder->page->$product_status")
                product_status = resultResponse
                Timber.d("$TAG getMyOrder->Favorite->$product_status")
                Timber.e("$TAG getMyOrder-> Resource.Success->$resultResponse")
                return Resource.Success(product_status ?: resultResponse)
            }
        }
        Timber.e("$TAG getMyOrder->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun ChangeProductStatus(updateStatus: UpdateStatus) = viewModelScope.launch {
        ChangeProductStatus(
            share.getString(com.nurbk.ps.homebusness.util.Constant.TOKEN, "").toString(),
            updateStatus
        )
    }


    init {
        getMyProduct()
        getMyOrder()
    }


    val dataStatusLiveData = MutableLiveData<Resource<Status>>()


    private suspend fun addStory(
        Authorization: String,
        images: MultipartBody.Part
    ) {
        dataStatusLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .addStory(
                    images,
                    Authorization,
                    share.getString(Constant.LANG, "ar").toString()
                )

            dataStatusLiveData.postValue(addStory(response))
            Timber.d("$TAG uploadAvater-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataStatusLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG uploadAvater-> Network Failure")
                }
                else -> {
                    dataStatusLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG uploadAvater-> Conversion Error")
                }

            }
        }
    }


    private fun addStory(response: Response<Status>):
            Resource<Status> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG PostCheckout->")
                Timber.d("$TAG PostCheckout-> Resource.Success->$resultResponse")
                return Resource.Success(resultResponse)
            }
        }
        Timber.e("$TAG PostCheckout->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun addStory(images: MultipartBody.Part) =
        viewModelScope.launch {
            addStory(
                share.getString(Constant.TOKEN, "").toString(),
                images
            )
        }
}