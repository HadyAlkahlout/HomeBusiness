package com.nurbk.ps.homebusness.model.packages

import com.google.gson.annotations.SerializedName

class PostProductPackage(
    @SerializedName("package_id")
    var package_id: Long,
    @SerializedName("product_id")
    var product_id: Long
)