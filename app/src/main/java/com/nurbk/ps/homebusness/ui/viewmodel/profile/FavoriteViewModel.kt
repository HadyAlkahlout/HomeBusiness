package com.nurbk.ps.homebusness.ui.viewmodel.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nurbk.ps.homebusness.model.favorite.Favorite
import com.nurbk.ps.homebusness.repoistory.ApiRepository
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    val TAG = "FavoriteViewModel"

    private val repository = ApiRepository()

    val dataFavoriteLiveData = MutableLiveData<Resource<Favorite>>()
    private var favorite: Favorite? = null

    private val share by lazy {
        Constant.getSharePref(application.applicationContext)
    }


    private var page = 1


    private suspend fun getFavorite(
        Authorization: String,lang:String
    ) {
        dataFavoriteLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getFavorite(Authorization, page.toString(),lang)

            dataFavoriteLiveData.postValue(getFavorite(response))
            Timber.e("$TAG getFavorite-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataFavoriteLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getFavorite-> Network Failure")
                }
                else -> {
                    dataFavoriteLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getFavorite-> Conversion Error")
                }

            }
        }
    }

    private fun getFavorite(response: Response<Favorite>):
            Resource<Favorite> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                page++
                Timber.e("$TAG getFavorite->page->$favorite")
                if (favorite == null) {
                    favorite = resultResponse
                    Timber.d("$TAG getFavorite->Favorite->$favorite")
                } else {
                    val oldImage = favorite
                    oldImage!!.data.data.addAll(resultResponse.data.data)
                    Timber.e("$TAG getFavorite->getFavorite->$oldImage")

                }
                Timber.e("$TAG getFavorite-> Resource.Success->$resultResponse")
                return Resource.Success(favorite ?: resultResponse)
            }
        }
        Timber.e("$TAG getFavorite->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun getFavorite() = viewModelScope.launch {
        getFavorite(
            share.getString(Constant.TOKEN, "").toString(),
            share.getString(Constant.LANG, "ar").toString(),
        )    }


    init {
        getFavorite()
    }
}