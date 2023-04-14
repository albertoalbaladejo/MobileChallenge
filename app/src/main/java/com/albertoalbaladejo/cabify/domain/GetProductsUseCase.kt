package com.albertoalbaladejo.cabify.domain

import com.albertoalbaladejo.cabify.data.ProductRepository
import com.albertoalbaladejo.cabify.data.database.entities.ProductEntity
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(private val repository: ProductRepository) {

    suspend operator fun invoke(): List<ProductEntity> {
        val products = repository.getAllProductsFromApi()

        if (products.products.isNotEmpty()) {
            val productsEntityList = products.products.map {
                ProductEntity(
                    it.code,
                    it.name,
                    it.price
                )
            }
            repository.clearProducts()
            repository.insertProducts(productsEntityList)
            return productsEntityList
        }

        return repository.getAllProductsFromDatabase().map {
            ProductEntity(
                it.code,
                it.name,
                it.price
            )
        }
    }
}