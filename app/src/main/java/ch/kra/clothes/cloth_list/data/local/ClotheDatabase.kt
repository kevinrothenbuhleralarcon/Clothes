package ch.kra.clothes.cloth_list.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ch.kra.clothes.cloth_list.data.local.converter.DateConverter
import ch.kra.clothes.cloth_list.data.local.dao.ClotheDao
import ch.kra.clothes.cloth_list.data.local.dao.UserListDao
import ch.kra.clothes.cloth_list.data.local.entitiy.ClotheEntity
import ch.kra.clothes.cloth_list.data.local.entitiy.UserListEntity

@Database(
    entities = [
        ClotheEntity::class,
        UserListEntity::class
    ],
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class ClotheDatabase: RoomDatabase() {
    abstract val clotheDao: ClotheDao
    abstract val userListDao: UserListDao
}