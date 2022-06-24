package ch.kra.wardrobe.cloth_list.data.local.entitiy

import androidx.room.Entity
import androidx.room.PrimaryKey
import ch.kra.wardrobe.cloth_list.domain.model.Clothe

@Entity
data class ClotheEntity(
    @PrimaryKey(autoGenerate = true) val clothId: Int = 0,
    val clothe: String,
    val quantity: Int,
    val typeId: Int,
    val userWardrobeId: Int
) {
    fun toClothe(): Clothe {
        return Clothe(
            id = clothId,
            clothe = clothe,
            quantity = quantity,
            typeId = typeId,
        )
    }
}
