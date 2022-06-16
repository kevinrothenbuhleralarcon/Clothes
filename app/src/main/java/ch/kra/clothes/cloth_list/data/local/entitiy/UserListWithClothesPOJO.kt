package ch.kra.clothes.cloth_list.data.local.entitiy

import androidx.room.Embedded
import androidx.room.Relation
import ch.kra.clothes.cloth_list.domain.model.UserListWithClothes

data class UserListWithClothesPOJO(
    @Embedded val userList: UserListEntity,
    @Relation(
        parentColumn = "userId",
        entityColumn = ""
    )
    val listClothe: List<ClotheEntity>
) {
    fun toUserListWithClothes(): UserListWithClothes {
        return UserListWithClothes(
            userList = userList.toUserList(),
            listClothe = listClothe.map { it.toClothe() }
        )
    }
}
