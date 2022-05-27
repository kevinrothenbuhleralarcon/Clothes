package ch.kra.clothes.cloth_list.data.local.entitiy

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_clothe_type")
data class ClotheTypeEntity(
    @PrimaryKey(autoGenerate = true) val clothTypeId: Int = 0,
    val type: String
)
