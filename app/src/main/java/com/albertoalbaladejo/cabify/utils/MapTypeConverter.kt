package com.albertoalbaladejo.cabify.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object MapTypeConverter {
    private val gson = Gson()

    @TypeConverter
    @JvmStatic
    fun stringToMap(value: String): Map<String, Double> {
        return gson.fromJson(value, object : TypeToken<Map<String, Double>>() {}.type)
    }

    @TypeConverter
    @JvmStatic
    fun mapToString(value: Map<String, Double>?): String {
        return if (value == null) "" else gson.toJson(value)
    }
}