package com.albertoalbaladejo.cabify.domain

import com.albertoalbaladejo.cabify.data.ProductRepository
import com.albertoalbaladejo.cabify.data.database.entities.BasketShoppingEntity
import com.albertoalbaladejo.cabify.utils.Result
import javax.inject.Inject

class GetBasketShoppingByIdUseCase @Inject constructor(private val repository: ProductRepository) {

    suspend operator fun invoke(code: String): Result<BasketShoppingEntity> {
        return repository.getBasketShoppingProductByIdNew(code)
    }
}