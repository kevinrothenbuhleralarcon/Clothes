package ch.kra.clothes.cloth_list.data.local.entitiy

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_clothe_type")
data class ClotheType(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String
)
