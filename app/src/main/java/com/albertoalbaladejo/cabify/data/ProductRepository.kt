package com.albertoalbaladejo.cabify.data

import com.albertoalbaladejo.cabify.data.database.dao.ProductDao
import com.albertoalbaladejo.cabify.data.database.entities.BasketShoppingEntity
import com.albertoalbaladejo.cabify.data.database.entities.OrdersEntity
import com.albertoalbaladejo.cabify.data.database.entities.ProductEntity
import com.albertoalbaladejo.cabify.data.model.Products
import com.albertoalbaladejo.cabify.data.network.ProductService
import com.albertoalbaladejo.cabify.di.DispatcherProvider
import com.albertoalbaladejo.cabify.domain.model.toDomain
import com.albertoalbaladejo.cabify.utils.Result
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val api: ProductService,
    private val productDao: ProductDao,
    private val dispatcherProvider: DispatcherProvider
) {

    suspend fun getAllProductsFromApi(): Products {
        return withContext(dispatcherProvider.io()) {
            api.getProducts()
        }
    }

    suspend fun getAllProductsFromDatabase(): List<ProductEntity> {
        return withContext(dispatcherProvider.io()) {
            productDao.getAllProducts()
        }
    }

    suspend fun getProductById(code: String): ProductEntity {
        return withContext(dispatcherProvider.io()) {
            productDao.getProductById(code)
        }
    }

    suspend fun getAllBasketShoppingProduct(): List<BasketShoppingEntity> =
        withContext(dispatcherProvider.io()) {
            val response: List<BasketShoppingEntity> = productDao.getAllBasketShoppingProduct()
            return@withContext response.map { it.toDomain() }
        }

    suspend fun getAllOrdersProduct(): List<OrdersEntity> =
        withContext(dispatcherProvider.io()) {
            val response: List<OrdersEntity> = productDao.getAllOrdersProduct()
            return@withContext response.map { it }
        }

    suspend fun getBasketShoppingProductById(code: String): BasketShoppingEntity {
        return withContext(dispatcherProvider.io()) {
            productDao.getBasketShoppingProductById(code)
        }
    }

    suspend fun getBasketShoppingProductByIdNew(code: String): Result<BasketShoppingEntity> =
        withContext(dispatcherProvider.io()) {
            try {
                val product = productDao.getBasketShoppingProductById(code)
                return@withContext Result.Success(product)
            } catch (e: Exception) {
                return@withContext Result.Error(e)
            }
        }

    suspend fun insertProducts(products: List<ProductEntity>) {
        withContext(dispatcherProvider.io()) {
            productDao.insertAll(products)
        }
    }

    suspend fun insertProduct(product: BasketShoppingEntity) =
        withContext(dispatcherProvider.io()) {
            productDao.insertProduct(product)
        }

    suspend fun insertOrder(order: OrdersEntity) = withContext(dispatcherProvider.io()) {
        productDao.insertOrder(order)
    }

    suspend fun updateProduct(product: BasketShoppingEntity) =
        withContext(dispatcherProvider.io()) {
            productDao.update(product)
        }

    suspend fun clearProducts() {
        withContext(dispatcherProvider.io()) {
            productDao.deleteAllProducts()
        }
    }

    suspend fun clearBasketShopping() {
        withContext(dispatcherProvider.io()) {
            productDao.deleteAllBasketShopping()
        }
    }

    suspend fun deleteProductById(code: String) = withContext(dispatcherProvider.io()) {
        productDao.deleteProductById(code)
    }
}