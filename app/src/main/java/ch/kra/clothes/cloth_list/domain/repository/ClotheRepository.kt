package ch.kra.clothes.cloth_list.domain.repository

import ch.kra.clothes.cloth_list.domain.model.UserList
import ch.kra.clothes.cloth_list.domain.model.UserListWithClothes
import kotlinx.coroutines.flow.Flow

interface ClotheRepository {
    fun getUsersList(): Flow<List<UserList>>

    fun getUserListWithClothById(id: Int): Flow<UserListWithClothes>

    suspend fun addUserListWithClothes(userListWithClothes: UserListWithClothes)

    suspend fun updateUserListWithClothes(userListWithClothes: UserListWithClothes)

    suspend fun deleteUserListWithClothes(userListId: Int)
}