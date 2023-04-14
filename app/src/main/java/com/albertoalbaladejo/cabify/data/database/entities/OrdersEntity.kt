package com.albertoalbaladejo.cabify.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.albertoalbaladejo.cabify.utils.DateTypeConverter
import com.albertoalbaladejo.cabify.utils.MapTypeConverter
import com.albertoalbaladejo.cabify.utils.ObjectListTypeConvertor
import java.util.*

private const val ESTADO_ENTREGADO = "Entregado"

@Entity(tableName = "orders_table")
data class OrdersEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "order_id") val orderId: String,
    @TypeConverters(ObjectListTypeConvertor::class)
    @ColumnInfo(name = "items") val items: List<BasketShoppingEntity>,
    @TypeConverters(MapTypeConverter::class)
    @ColumnInfo(name = "items_prices") val itemsPrices: Map<String, Double>,
    @TypeConverters(DateTypeConverter::class)
    @ColumnInfo(name = "order_date") val orderDate: Date,
    @ColumnInfo(name = "status") val status: String = ESTADO_ENTREGADO,
)