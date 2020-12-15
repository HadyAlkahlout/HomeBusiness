package com.nurbk.ps.homebusness.ui.viewmodel.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nurbk.ps.homebusness.model.question.QuestionAnswer
import com.nurbk.ps.homebusness.repoistory.ApiRepository
import com.nurbk.ps.homebusness.util.Constant
import com.nurbk.ps.homebusness.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class QuestionViewModel(application: Application) : AndroidViewModel(application) {


    val TAG = "QuestionViewModel"

    private val repository = ApiRepository()

    val dataQuestionLiveData = MutableLiveData<Resource<QuestionAnswer>>()
    private var question: QuestionAnswer? = null

    private val share by lazy {
        Constant.getSharePref(application.applicationContext)
    }


    private suspend fun getQuestion(
        Authorization: String
    ) {
        dataQuestionLiveData.postValue(Resource.Loading())
        try {
            val response = repository
                .getQuestion(Authorization, share.getString(Constant.LANG, "ar").toString())

            dataQuestionLiveData.postValue(getQuestion(response))
            Timber.e("$TAG getQuestion-> OK")
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    dataQuestionLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getQuestion-> Network Failure")
                }
                else -> {
                    dataQuestionLiveData.postValue(Resource.Error(t.message.toString()))
                    Timber.e("$TAG getQuestion-> Conversion Error")
                }

            }
        }
    }

    private fun getQuestion(response: Response<QuestionAnswer>):
            Resource<QuestionAnswer> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->

                Timber.e("$TAG v->page->$question")
                question = resultResponse
                Timber.d("$TAG getQuestion->Favorite->$question")

                Timber.e("$TAG getQuestion-> Resource.Success->$resultResponse")
                return Resource.Success(question ?: resultResponse)
            }
        }
        Timber.e("$TAG getQuestion->Resource.Error->${response.message()}")
        return Resource.Error(response.message())
    }


    fun getQuestion() = viewModelScope.launch {
        getQuestion(
            share.getString(Constant.TOKEN, "").toString()
        )
    }


    init {
        getQuestion()
    }


}