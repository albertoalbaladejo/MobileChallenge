package com.albertoalbaladejo.cabify.domain

import com.albertoalbaladejo.cabify.data.ProductRepository
import com.albertoalbaladejo.cabify.data.database.entities.ProductEntity
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(private val repository: ProductRepository) {

    suspend operator fun invoke(code: String): ProductEntity {
        return repository.getProductById(code)
    }
}