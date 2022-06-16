package ch.kra.clothes.cloth_list.data.local.entitiy

import androidx.room.Entity
import androidx.room.PrimaryKey
import ch.kra.clothes.cloth_list.domain.model.Clothe

@Entity
data class ClotheEntity(
    @PrimaryKey(autoGenerate = true) val clothId: Int = 0,
    val clothe: String,
    val quantity: Int,
    val typeId: Int,
    val userListId: Int
) {
    fun toClothe(): Clothe {
        return Clothe(
            id = clothId,
            clothe = clothe,
            quantity = quantity,
            typeId = typeId,
            userListId = userListId
        )
    }
}
