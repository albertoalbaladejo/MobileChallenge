package com.albertoalbaladejo.cabify.utils

import androidx.room.TypeConverter
import java.util.*

class DateTypeConverter {
	@TypeConverter
	fun toDate(dateLong: Long?): Date? = dateLong?.let { Date(it) }

	@TypeConverter
	fun fromDate(date: Date?): Long? = date?.time
}