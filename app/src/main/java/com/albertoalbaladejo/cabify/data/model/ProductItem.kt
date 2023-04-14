package com.albertoalbaladejo.cabify.data.model

import com.google.gson.annotations.SerializedName

data class ProductItem(
    @SerializedName("code")
    val code: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: Double
)