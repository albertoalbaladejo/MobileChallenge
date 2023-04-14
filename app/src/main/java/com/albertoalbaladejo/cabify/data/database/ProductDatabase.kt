package com.albertoalbaladejo.cabify.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.albertoalbaladejo.cabify.data.database.dao.ProductDao
import com.albertoalbaladejo.cabify.data.database.entities.BasketShoppingEntity
import com.albertoalbaladejo.cabify.data.database.entities.OrdersEntity
import com.albertoalbaladejo.cabify.data.database.entities.ProductEntity
import com.albertoalbaladejo.cabify.utils.DateTypeConverter
import com.albertoalbaladejo.cabify.utils.MapTypeConverter
import com.albertoalbaladejo.cabify.utils.ObjectListTypeConvertor

@Database(
    entities = [ProductEntity::class, BasketShoppingEntity::class, OrdersEntity::class],
    version = 1
)
@TypeConverters(MapTypeConverter::class, ObjectListTypeConvertor::class, DateTypeConverter::class)
abstract class ProductDatabase : RoomDatabase() {

    abstract fun getProductDao(): ProductDao
}