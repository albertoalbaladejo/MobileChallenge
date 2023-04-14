package com.albertoalbaladejo.cabify.data.network

import com.albertoalbaladejo.cabify.data.model.Products
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductService @Inject constructor(private val api: ProductApiClient) {

    suspend fun getProducts(): Products {
        return withContext(Dispatchers.IO) {
            val response = api.getAllProducts()
            response.body()!!
        }
    }
}