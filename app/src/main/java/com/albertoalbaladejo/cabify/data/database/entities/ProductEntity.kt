package com.albertoalbaladejo.cabify.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.albertoalbaladejo.cabify.strategy.HasDiscountCode

@Entity(tableName = "product_table")
data class ProductEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "code") @JvmField val code: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "price") val price: Double
) : HasDiscountCode {
    override fun getCode(): String = code
}