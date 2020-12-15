package com.nurbk.ps.homebusness.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nurbk.ps.homebusness.model.contact.Contact
import com.nurbk.ps.homebusness.model.contact.ContactMarket
import com.nurbk.ps.homebusness.model.status.Status
import com.nurbk.ps.homebusness.repoistory.ApiRepository
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Constant.TOKEN
import com.nurbk.ps.homebusness.util.Constant.getSharePref
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "ContactUsViewModel"

    private val repository = ApiRepository()

    val contactLiveData = MutableLiveData<Resource<Status>>()
    private var contact: Status? = null
    private val share = getSharePref(application.applicationContext)


    private suspend fun postContactUs(
        Authorization: String,
        contact: Contact, type: Int,
        contactMarket: ContactMarket,
    ) {
        contactLiveData.postValue(Resource.Loading())
        try {
            val response =
                if (type == 1)
                    repository
                        .postContactUs(
                            contact,
                            Authorization,
                            share.getString(Constant.LANG, "ar").toString()
                        )
                else
                    repository
                        .sendMarket(
                            contactMarket,
                            Authorization,
                            share.getString(Constant.LANG, "ar").toString()
                        )

            contactLiveData.postValue(postCon(response))
            Timber.d("$TAG getPrivacy-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    contactLiveData.postValue(Resource.Error("Network Failure"))
                    Timber.e("$TAG getPrivacy-> Network Failure")
                }
                else -> {
                    contactLiveData.postValue(Resource.Error("Conversion Error"))
                    Timber.e("$TAG getPrivacy-> Conversion Error")
                }

            }
        }
    }


    private fun postCon(response: Response<Status>):
            Resource<Status> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Timber.e("$TAG getContact->")
                contact = resultResponse
                Timber.e("$TAG getContact->response->$contact")
                Timber.e("$TAG getContact-> Resource.Success->$resultResponse")
                return Resource.Success(contact ?: resultResponse)
            }
        }
        Timber.e("$TAG getContact->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun postContact(contact: Contact, type: Int, contactMarket: ContactMarket) =
        viewModelScope.launch {
            postContactUs(
                share.getString(Constant.TOKEN, "").toString(),
                contact, type, contactMarket
            )
        }


}