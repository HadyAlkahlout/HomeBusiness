package com.nurbk.ps.homebusness.model.question


import com.google.gson.annotations.SerializedName
import com.nurbk.ps.homebusness.model.question.Data

data class QuestionAnswer(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("status")
    var status: Boolean
)