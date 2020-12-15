package com.nurbk.ps.homebusness.ui.viewmodel.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nurbk.ps.homebusness.model.user.ActivateAccount
import com.nurbk.ps.homebusness.model.user.RegisterUser
import com.nurbk.ps.homebusness.model.user.resendActivtion.Resend
import com.nurbk.ps.homebusness.model.user.userActivate.DataActivate
import com.nurbk.ps.homebusness.repoistory.ApiRepository
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Constant.TOKEN
import com.nurbk.ps.homebusness.util.Constant.getSharePref
import com.nurbk.ps.homebusness.util.Resource
import com.raiyansoft.eata.model.user.UserToken
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "AuthViewModel"
    private val repository = ApiRepository()


    val dataUserLiveData = MutableLiveData<Resource<UserToken>>()
    private var userToken: UserToken? = null

    private val share = getSharePref(application.applicationContext)


    private suspend fun registerUser(
        registerUser: RegisterUser
    ) {
        dataUserLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .registerUser(registerUser)

            dataUserLiveData.postValue(registerUser(response))
            Timber.d("$TAG registerUser-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataUserLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG registerUser-> ${t.message.toString()}")
                }
                else -> {
                    dataUserLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG registerUser-> ${t.message.toString()}")
                }

            }
        }
    }

    private fun registerUser(response: Response<UserToken>):
            Resource<UserToken> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG registerUser->")
                userToken = resultResponse
                return Resource.Success(userToken ?: resultResponse)
            }
        }
        Timber.e("$TAG registerUser->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun registerUsers(
        registerUser: RegisterUser
    ) =
        viewModelScope.launch {
            registerUser(
                registerUser
            )
        }



    val dataStatusLiveData = MutableLiveData<Resource<DataActivate>>()
    private var activate: DataActivate? = null


    private suspend fun activateAccount(
        Authorization: String, activateAccount: ActivateAccount
    ) {
        dataStatusLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .activateAccount(Authorization, activateAccount, share.getString(Constant.LANG, "ar").toString())

            dataStatusLiveData.postValue(activateAccount(response))
            Timber.d("$TAG postBenefactor-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataStatusLiveData.postValue(Resource.Error("Network Failure"))
                    Timber.e("$TAG postBenefactor-> Network Failure")
                }
                else -> {
                    dataStatusLiveData.postValue(Resource.Error("Conversion Error"))
                    Timber.e("$TAG postBenefactor-> Conversion Error")
                }

            }
        }
    }

    private fun activateAccount(response: Response<DataActivate>):
            Resource<DataActivate> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG activateAccount->")
                activate = resultResponse
                Timber.d("$TAG activateAccount->response->$activate")
                Timber.d("$TAG activateAccount-> Resource.Success->$resultResponse")
                return Resource.Success(activate ?: resultResponse)
            }
        }
        Timber.e("$TAG activateAccount->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun activateAccount(
        activateAccount: ActivateAccount
    ) =
        viewModelScope.launch {
            activateAccount(
                share.getString(TOKEN, "")!!, activateAccount
            )
        }



    val dataResendLiveData = MutableLiveData<Resource<Resend>>()
    private var resend: Resend? = null


    private suspend fun resendActivation(
        Authorization: String
    ) {
        dataResendLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .resendActivation(Authorization, share.getString(Constant.LANG, "ar").toString())

            dataResendLiveData.postValue(resendActivation(response))
            Timber.d("$TAG resendActivation-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataResendLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG resendActivation-> Network Failure")
                }
                else -> {
                    dataResendLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG resendActivation-> Conversion Error")
                }

            }
        }
    }

    private fun resendActivation(response: Response<Resend>):
            Resource<Resend> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG resendActivation->")
                resend = resultResponse
                Timber.d("$TAG resendActivation->response->$resend")
                Timber.d("$TAG resendActivation-> Resource.Success->$resultResponse")
                return Resource.Success(resend ?: resultResponse)
            }
        }
        Timber.e("$TAG activateAccount->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun resendActivation(
    ) =
        viewModelScope.launch {
            resendActivation(
                share.getString(TOKEN, "")!!
            )
        }


}