package ch.kra.clothes.cloth_list.data.local.entitiy

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_clothe_list")
data class ClotheListEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val clothe: String,
    val quantity: Int,
    @ColumnInfo(name = "type_id") val typeId: Int,
    @ColumnInfo(name = "user_list_id") val userListId: Int,
)
