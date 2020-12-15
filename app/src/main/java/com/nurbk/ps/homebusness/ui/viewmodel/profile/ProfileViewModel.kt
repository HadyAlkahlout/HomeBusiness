package com.nurbk.ps.homebusness.ui.viewmodel.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nurbk.ps.homebusness.model.avatar.UpdateAvatar
import com.nurbk.ps.homebusness.model.profile.Profile
import com.nurbk.ps.homebusness.model.profile.UpdateProfile
import com.nurbk.ps.homebusness.model.status.Status
import com.nurbk.ps.homebusness.repoistory.ApiRepository
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    val TAG = "ProfileViewModel"

    private val repository = ApiRepository()

    val dataProfileLiveData = MutableLiveData<Resource<Profile>>()
    private var profile: Profile? = null

    val dataUpdateProfileLiveData = MutableLiveData<Resource<Profile>>()
    val dataUpdateLiveData = MutableLiveData<Resource<UpdateAvatar>>()
    private var updateAvter: UpdateAvatar? = null
    private val share by lazy {
        Constant.getSharePref(application.applicationContext)
    }

    private var page = 1


    private suspend fun getProfile(
        Authorization: String
    ) {
        dataProfileLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getProfile(Authorization, share.getString(Constant.LANG, "ar").toString())

            dataProfileLiveData.postValue(getProfile(response))
            Timber.e("$TAG getMyProfile-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataProfileLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getMyProfile-> Network Failure")
                }
                else -> {
                    dataProfileLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getMyProfile-> Conversion Error")
                }

            }
        }
    }

    private fun getProfile(response: Response<Profile>):
            Resource<Profile> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                page++
                Timber.e("$TAG getMyProfile->page->$profile")
                profile = resultResponse
                Timber.d("$TAG getMyProfile->Favorite->$profile")

                Timber.e("$TAG getMyProfile-> Resource.Success->$resultResponse")
                return Resource.Success(profile ?: resultResponse)
            }
        }
        Timber.e("$TAG getMyProfile->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun getProfile() = viewModelScope.launch {
        getProfile(
            share.getString(Constant.TOKEN, "").toString()
        )
    }


    private suspend fun uploadNewAvater(
        Authorization: String,
        images: MultipartBody.Part
    ) {
        dataUpdateLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .updateAvatar(
                    Authorization,
                    images,
                    share.getString(Constant.LANG, "ar").toString()
                )

            dataUpdateLiveData.postValue(uploadNewAvater(response))
            Timber.d("$TAG uploadAvater-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataUpdateLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG uploadAvater-> Network Failure")
                }
                else -> {
                    dataUpdateLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG uploadAvater-> Conversion Error")
                }

            }
        }
    }

    private fun uploadNewAvater(response: Response<UpdateAvatar>):
            Resource<UpdateAvatar> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG uploadAvater->")
                updateAvter = resultResponse
                Timber.d("$TAG uploadAvater->response->$updateAvter")
                Timber.d("$TAG uploadAvater-> Resource.Success->$resultResponse")
                return Resource.Success(updateAvter ?: resultResponse)
            }
        }
        Timber.e("$TAG uploadAvater->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun uploadNewAvater(images: MultipartBody.Part) =
        viewModelScope.launch {
            uploadNewAvater(
                share.getString(Constant.TOKEN, "").toString(), images
            )
        }


    /// update profile

    private suspend fun updateProfileAccount(
        Authorization: String,
        updateProfile: UpdateProfile
    ) {
        dataUpdateLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .updateProfileAccount(
                    Authorization,
                    share.getString(Constant.LANG, "ar").toString(),
                    updateProfile
                )

            dataUpdateProfileLiveData.postValue(updateProfileAccount(response))
            Timber.d("$TAG uploadAvater-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataUpdateProfileLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG uploadAvater-> Network Failure")
                }
                else -> {
                    dataUpdateProfileLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG uploadAvater-> Conversion Error")
                }

            }
        }
    }

    private fun updateProfileAccount(response: Response<Profile>):
            Resource<Profile> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.d("$TAG uploadAvater->")
                profile = resultResponse
                Timber.d("$TAG uploadAvater->response->$profile")
                Timber.d("$TAG uploadAvater-> Resource.Success->$resultResponse")
                return Resource.Success(profile ?: resultResponse)
            }
        }
        Timber.e("$TAG uploadAvater->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun updateProfileAccount(updateProfile: UpdateProfile) =
        viewModelScope.launch {
            updateProfileAccount(
                share.getString(com.nurbk.ps.homebusness.util.Constant.TOKEN, "").toString(),
                updateProfile
            )
        }


    init {
        getProfile()
    }



}