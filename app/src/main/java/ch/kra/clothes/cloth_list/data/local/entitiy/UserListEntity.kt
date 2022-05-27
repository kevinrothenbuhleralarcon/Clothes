package ch.kra.clothes.cloth_list.data.local.entitiy

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "tbl_user_list")
data class UserListEntity(
    @PrimaryKey(autoGenerate = true) val userId: Int = 0,
    val username: String,
    val location: String,
    @ColumnInfo(name = "last_updated") val lastUpdated: Date
)
