package com.nurbk.ps.homebusness.model.user.resendActivtion


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("resend_code_count")
    var resendCodeCount: Int
)