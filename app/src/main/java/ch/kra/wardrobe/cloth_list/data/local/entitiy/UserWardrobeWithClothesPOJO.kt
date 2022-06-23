package ch.kra.wardrobe.cloth_list.data.local.entitiy

import androidx.room.Embedded
import androidx.room.Relation
import ch.kra.wardrobe.cloth_list.domain.model.UserWardrobeWithClothes

data class UserWardrobeWithClothesPOJO(
    @Embedded val userWardrobe: UserWardrobeEntity,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userWardrobeId"
    )
    val listClothe: List<ClotheEntity>
) {
    fun toUserListWithClothes(): UserWardrobeWithClothes {
        return UserWardrobeWithClothes(
            userWardrobe = userWardrobe.toUserList(),
            listClothe = listClothe.map { it.toClothe() }
        )
    }
}
