package ch.kra.wardrobe.cloth_list.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ch.kra.wardrobe.cloth_list.data.local.converter.ClotheTypeConverter
import ch.kra.wardrobe.cloth_list.data.local.converter.DateConverter
import ch.kra.wardrobe.cloth_list.data.local.dao.ClotheDao
import ch.kra.wardrobe.cloth_list.data.local.dao.UserWardrobeDao
import ch.kra.wardrobe.cloth_list.data.local.entitiy.ClotheEntity
import ch.kra.wardrobe.cloth_list.data.local.entitiy.UserWardrobeEntity

@Database(
    entities = [
        ClotheEntity::class,
        UserWardrobeEntity::class
    ],
    version = 3
)
@TypeConverters(DateConverter::class, ClotheTypeConverter::class)
abstract class WardrobeDatabase: RoomDatabase() {
    abstract val clotheDao: ClotheDao
    abstract val userWardrobeDao: UserWardrobeDao
}