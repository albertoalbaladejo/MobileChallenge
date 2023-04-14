package com.albertoalbaladejo.cabify.domain

import com.albertoalbaladejo.cabify.data.ProductRepository
import com.albertoalbaladejo.cabify.data.database.entities.OrdersEntity
import javax.inject.Inject

class InsertOrderUseCase @Inject constructor(private val repository: ProductRepository) {

    suspend operator fun invoke(order: OrdersEntity): Boolean {
        return try {
            repository.insertOrder(order)
            true
        } catch (e: Exception) {
            false
        }
    }
}