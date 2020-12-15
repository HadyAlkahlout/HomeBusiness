package com.nurbk.ps.homebusness.ui.viewmodel.cart

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nurbk.ps.homebusness.model.payment.Payment
import com.nurbk.ps.homebusness.model.promocode.Data
import com.nurbk.ps.homebusness.model.promocode.PromoCode
import com.nurbk.ps.homebusness.model.status.Status
import com.nurbk.ps.homebusness.repoistory.ApiRepository
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class CheckoutViewModel(application: Application) : AndroidViewModel(application) {

    val TAG = "CheckoutViewModel"

    private val repository = ApiRepository()

    val dataCheckoutLiveData = MutableLiveData<Resource<Status>>()
    val dataPaymentLiveData = MutableLiveData<Resource<Payment>>()
    val dataPromoCodeLiveData = MutableLiveData<Resource<PromoCode>>()
    private var status: Status? = null
    private var payment:Payment? = null
    private var promoCode:PromoCode? = null


    private val share by lazy {
        Constant.getSharePref(application.applicationContext)
    }


    private suspend fun PostCheckout(
        params: Map<String, RequestBody>, Authorization: String
    ) {
        dataCheckoutLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .postCheckout(params, Authorization, share.getString(Constant.LANG, "ar").toString())

            dataCheckoutLiveData.postValue(PostCheckout(response))
            Timber.d("$TAG PostCheckout-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataCheckoutLiveData.postValue(Resource.Error("Network Failure"))
                    Timber.e("$TAG PostCheckout-> Network Failure")
                }
                else -> {
                    dataCheckoutLiveData.postValue(Resource.Error("Conversion Error"))
                    Timber.e("$TAG PostCheckout-> Conversion Error")
                }

            }
        }
    }


    private fun PostCheckout(response: Response<Status>):
            Resource<Status> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG PostCheckout->")
                status = resultResponse
                Timber.d("$TAG PostCheckout->response->$status")
                Timber.d("$TAG PostCheckout-> Resource.Success->$resultResponse")
                return Resource.Success(status ?: resultResponse)
            }
        }
        Timber.e("$TAG PostCheckout->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun PostCheckout( params: Map<String, RequestBody>,) =
        viewModelScope.launch {
            PostCheckout(
                params,share.getString(Constant.TOKEN,"").toString()
            )
            Log.e("eee Token",share.getString(Constant.TOKEN,"").toString())
        }



    // get payment Methoud

    private suspend fun getPayment(
        Authorization: String
    ) {
        dataPaymentLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getPayment(Authorization, share.getString(Constant.LANG, "ar").toString())

            dataPaymentLiveData.postValue(getPayment(response))
            Timber.d("$TAG getPayment-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataCheckoutLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getPayment-> Network Failure")
                }
                else -> {
                    dataCheckoutLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getPayment-> Conversion Error")
                }

            }
        }
    }

    private fun getPayment(response: Response<Payment>):
            Resource<Payment> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG getPayment->")
                payment = resultResponse
                Timber.d("$TAG getPayment->response->$payment")
                Timber.d("$TAG getPayment-> Resource.Success->$resultResponse")
                return Resource.Success(payment ?: resultResponse)
            }
        }
        Timber.e("$TAG getPayment->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun getPayment() =
        viewModelScope.launch {
            getPayment(
                share.getString(Constant.TOKEN,"").toString()
            )
        }




    ///


    private suspend fun PostPromoCode(
            data: Data, Authorization: String
    ) {
        dataPromoCodeLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                    .postPromoCode(data, Authorization, share.getString(Constant.LANG, "ar").toString())

            dataPromoCodeLiveData.postValue(PostPromoCode(response))
            Timber.d("$TAG PostCheckout-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataPromoCodeLiveData.postValue(Resource.Error("Network Failure"))
                    Timber.e("$TAG PostCheckout-> Network Failure")
                }
                else -> {
                    dataPromoCodeLiveData.postValue(Resource.Error("Conversion Error"))
                    Timber.e("$TAG PostCheckout-> Conversion Error")
                }

            }
        }
    }


    private fun PostPromoCode(response: Response<PromoCode>):
            Resource<PromoCode> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG PostCheckout->")
                promoCode = resultResponse
                Timber.d("$TAG PostCheckout->response->$promoCode")
                Timber.d("$TAG PostCheckout-> Resource.Success->$resultResponse")
                return Resource.Success(promoCode ?: resultResponse)
            }
        }
        Timber.e("$TAG PostCheckout->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun PostPromoCode(code: Data) =
            viewModelScope.launch {
                PostPromoCode(
                        code,share.getString(Constant.TOKEN,"").toString()
                )
            }


    init {
        getPayment()
    }



}