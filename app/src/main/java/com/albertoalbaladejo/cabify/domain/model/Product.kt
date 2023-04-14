package com.albertoalbaladejo.cabify.domain.model

import com.albertoalbaladejo.cabify.data.database.entities.BasketShoppingEntity
import com.albertoalbaladejo.cabify.data.database.entities.ProductEntity

data class Product(val code: String, val name: String, val price: Double)

fun ProductEntity.toDomain() = Product(code, name, price)
fun BasketShoppingEntity.toDomain() = BasketShoppingEntity(code, name, price, quantity)