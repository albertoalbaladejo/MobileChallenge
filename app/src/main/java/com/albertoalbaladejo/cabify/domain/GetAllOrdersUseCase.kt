package com.albertoalbaladejo.cabify.domain

import com.albertoalbaladejo.cabify.data.ProductRepository
import com.albertoalbaladejo.cabify.data.database.entities.OrdersEntity
import javax.inject.Inject

class GetAllOrdersUseCase @Inject constructor(private val repository: ProductRepository) {

    suspend operator fun invoke(): List<OrdersEntity> {
        return repository.getAllOrdersProduct()
    }
}