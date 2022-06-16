package ch.kra.clothes.cloth_list.domain.model

data class UserListWithClothes(
    val userList: UserList,
    val listClothe: List<Clothe>
)
