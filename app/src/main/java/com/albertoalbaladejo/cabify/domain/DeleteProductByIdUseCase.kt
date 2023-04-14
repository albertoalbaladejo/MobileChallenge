package com.albertoalbaladejo.cabify.domain

import com.albertoalbaladejo.cabify.data.ProductRepository
import com.albertoalbaladejo.cabify.utils.Result
import com.albertoalbaladejo.cabify.utils.Result.Error
import com.albertoalbaladejo.cabify.utils.Result.Success
import javax.inject.Inject

class DeleteProductByIdUseCase @Inject constructor(private val repository: ProductRepository) {

    suspend operator fun invoke(code: String): Result<Boolean> {
        return try {
            repository.deleteProductById(code)
            Success(true)
        } catch (e: Exception) {
            Error(e)
        }
    }
}