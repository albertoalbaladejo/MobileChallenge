package com.albertoalbaladejo.cabify.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.albertoalbaladejo.cabify.strategy.HasDiscountCode

@Entity(tableName = "basket_table")
data class BasketShoppingEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "code") @JvmField var code: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "price") val price: Double,
    @ColumnInfo(name = "quantity") var quantity: Int
) : HasDiscountCode {
    override fun getCode(): String = code
}