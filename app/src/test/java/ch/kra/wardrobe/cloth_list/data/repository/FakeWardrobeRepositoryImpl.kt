package ch.kra.wardrobe.cloth_list.data.repository

import ch.kra.wardrobe.cloth_list.domain.model.UserWardrobe
import ch.kra.wardrobe.cloth_list.domain.model.UserWardrobeWithClothes
import ch.kra.wardrobe.cloth_list.domain.repository.WardrobeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeWardrobeRepositoryImpl: WardrobeRepository {
    val listUserWardrobeWithClothes = mutableListOf<UserWardrobeWithClothes>()

    override fun getWardrobes(): Flow<List<UserWardrobe>> = flow {
        emit(listUserWardrobeWithClothes.map { it.userWardrobe })
    }

    override fun getWardrobeWithClothesById(id: Int): Flow<UserWardrobeWithClothes?> = flow {
        emit(listUserWardrobeWithClothes.find { it.userWardrobe.id == id })
    }

    override suspend fun addWardrobeWithClothes(userWardrobeWithClothes: UserWardrobeWithClothes) {
        listUserWardrobeWithClothes.add(userWardrobeWithClothes)
    }

    override suspend fun updateWardrobeWithClothes(userWardrobeWithClothes: UserWardrobeWithClothes) {
        listUserWardrobeWithClothes.replaceAll {
            if (it.userWardrobe.id == userWardrobeWithClothes.userWardrobe.id)
                userWardrobeWithClothes
            else
                it
        }
    }

    override suspend fun deleteWardrobeWithClothes(userListId: Int) {
        listUserWardrobeWithClothes.remove(listUserWardrobeWithClothes.find { it.userWardrobe.id == userListId })
    }
}