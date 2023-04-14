package com.albertoalbaladejo.cabify.utils

import androidx.room.TypeConverter
import com.albertoalbaladejo.cabify.data.database.entities.BasketShoppingEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ObjectListTypeConvertor {

    private val gson = Gson()

    @TypeConverter
    fun stringToBasketShoppingList(data: String?): List<BasketShoppingEntity> {
        if (data.isNullOrBlank()) {
            return emptyList()
        }
        val listType = object : TypeToken<List<BasketShoppingEntity>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun basketShoppingObjectListToString(basketShoppingEntity: List<BasketShoppingEntity>): String {
        if (basketShoppingEntity.isEmpty()) {
            return ""
        }
        val listType = object : TypeToken<List<BasketShoppingEntity>>() {}.type
        return gson.toJson(basketShoppingEntity, listType)
    }
}