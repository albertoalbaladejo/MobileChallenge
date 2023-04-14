package com.albertoalbaladejo.cabify.domain

import com.albertoalbaladejo.cabify.data.ProductRepository
import com.albertoalbaladejo.cabify.data.database.entities.BasketShoppingEntity
import javax.inject.Inject

class SaveProductUseCase @Inject constructor(private val repository: ProductRepository) {

    suspend operator fun invoke(product: BasketShoppingEntity): Boolean {
        return runCatching { repository.insertProduct(product) }.isSuccess
    }
}