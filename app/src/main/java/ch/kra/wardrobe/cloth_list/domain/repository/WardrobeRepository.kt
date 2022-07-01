package ch.kra.wardrobe.cloth_list.domain.repository

import ch.kra.wardrobe.cloth_list.domain.model.UserWardrobe
import ch.kra.wardrobe.cloth_list.domain.model.UserWardrobeWithClothes
import kotlinx.coroutines.flow.Flow

interface WardrobeRepository {
    fun getWardrobes(): Flow<List<UserWardrobe>>

    fun getWardrobeWithClothesById(id: Int): Flow<UserWardrobeWithClothes?>

    suspend fun addWardrobeWithClothes(userWardrobeWithClothes: UserWardrobeWithClothes)

    suspend fun updateWardrobeWithClothes(userWardrobeWithClothes: UserWardrobeWithClothes)

    suspend fun deleteWardrobeWithClothes(userListId: Int)
}