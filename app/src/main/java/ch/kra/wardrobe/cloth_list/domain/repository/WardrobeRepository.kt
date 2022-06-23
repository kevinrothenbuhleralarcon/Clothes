package ch.kra.wardrobe.cloth_list.domain.repository

import ch.kra.wardrobe.cloth_list.domain.model.UserWardrobe
import ch.kra.wardrobe.cloth_list.domain.model.UserWardrobeWithClothes
import kotlinx.coroutines.flow.Flow

interface WardrobeRepository {
    fun getUsersList(): Flow<List<UserWardrobe>>

    fun getUserListWithClothById(id: Int): Flow<UserWardrobeWithClothes>

    suspend fun addUserListWithClothes(userWardrobeWithClothes: UserWardrobeWithClothes)

    suspend fun updateUserListWithClothes(userWardrobeWithClothes: UserWardrobeWithClothes)

    suspend fun deleteUserListWithClothes(userListId: Int)
}