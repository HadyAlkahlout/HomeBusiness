package com.nurbk.ps.homebusness.model.checkout

import com.google.gson.annotations.SerializedName

data class postCheckout (
    @SerializedName("products")
    val products: ArrayList<Product>,
    @SerializedName("payment_id")
    val payment_id: Long,
    @SerializedName("address_id")
    val address_id: Long,
    @SerializedName("notes")
    val notes: String

)


data class Product(
    @SerializedName("item_id")
    val item_id: Long,
    @SerializedName("number")
    val number: Long,
    @SerializedName("note")
    val note: Long,
)