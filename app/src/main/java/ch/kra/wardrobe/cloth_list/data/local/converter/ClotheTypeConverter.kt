package ch.kra.wardrobe.cloth_list.data.local.converter

import androidx.room.TypeConverter
import ch.kra.wardrobe.core.ClotheType

class ClotheTypeConverter {
    @TypeConverter
    fun fromStringToClotheType(value: String) = enumValueOf<ClotheType>(value)

    @TypeConverter
    fun toStringFromClotheType(value: ClotheType) = value.name
}