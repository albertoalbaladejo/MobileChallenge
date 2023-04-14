package com.albertoalbaladejo.cabify.data.database.dao

import androidx.room.*
import com.albertoalbaladejo.cabify.data.database.entities.BasketShoppingEntity
import com.albertoalbaladejo.cabify.data.database.entities.OrdersEntity
import com.albertoalbaladejo.cabify.data.database.entities.ProductEntity

@Dao
interface ProductDao {

    /** PRODUCT **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: BasketShoppingEntity)

    @Query("SELECT * FROM product_table ORDER BY code DESC")
    suspend fun getAllProducts(): List<ProductEntity>

    @Query("SELECT * FROM product_table WHERE code = :code")
    suspend fun getProductById(code: String): ProductEntity

    @Query("DELETE FROM product_table")
    suspend fun deleteAllProducts()

    /** BASKET SHOPPING **/
    @Query("SELECT * FROM basket_table")
    suspend fun getAllBasketShoppingProduct(): List<BasketShoppingEntity>

    @Query("SELECT * FROM basket_table WHERE code = :code")
    suspend fun getBasketShoppingProductById(code: String): BasketShoppingEntity

    @Query("DELETE FROM basket_table WHERE code = :code")
    suspend fun deleteProductById(code: String): Int

    @Query("DELETE FROM basket_table")
    suspend fun deleteAllBasketShopping()

    @Update
    suspend fun update(product: BasketShoppingEntity)

    /** ORDERS **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrdersEntity)

    @Query("SELECT * FROM orders_table")
    suspend fun getAllOrdersProduct(): List<OrdersEntity>
}