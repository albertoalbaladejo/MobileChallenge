package com.albertoalbaladejo.cabify.domain

import com.albertoalbaladejo.cabify.data.ProductRepository
import com.albertoalbaladejo.cabify.data.database.entities.BasketShoppingEntity
import javax.inject.Inject

class GetBasketShoppingByIdInProductUseCase @Inject constructor(private val repository: ProductRepository) {

    suspend operator fun invoke(code: String): BasketShoppingEntity {
        return repository.getBasketShoppingProductById(code)
    }
}