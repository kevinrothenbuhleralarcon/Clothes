package ch.kra.clothes.cloth_list.data.local.converter

import androidx.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter
    fun fromTimestampToDate(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun toTimestampFromDate(value: Date): Long {
        return value.time
    }
}