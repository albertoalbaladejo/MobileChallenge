package com.albertoalbaladejo.cabify.data.model

import com.google.gson.annotations.SerializedName

data class Products(
    @field:SerializedName("products")
    val products: List<ProductItem>
)

