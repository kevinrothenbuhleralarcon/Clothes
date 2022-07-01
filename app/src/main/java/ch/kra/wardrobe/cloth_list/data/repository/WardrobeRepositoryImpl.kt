package ch.kra.wardrobe.cloth_list.data.repository

import android.util.Log
import ch.kra.wardrobe.cloth_list.data.local.dao.ClotheDao
import ch.kra.wardrobe.cloth_list.data.local.dao.UserWardrobeDao
import ch.kra.wardrobe.cloth_list.data.local.entitiy.ClotheEntity
import ch.kra.wardrobe.cloth_list.data.local.entitiy.UserWardrobeEntity
import ch.kra.wardrobe.cloth_list.domain.model.UserWardrobe
import ch.kra.wardrobe.cloth_list.domain.model.UserWardrobeWithClothes
import ch.kra.wardrobe.cloth_list.domain.repository.WardrobeRepository
import kotlinx.coroutines.flow.*

class WardrobeRepositoryImpl(
    private val clotheDao: ClotheDao,
    private val userWardrobeDao: UserWardrobeDao
): WardrobeRepository {
    override fun getWardrobes(): Flow<List<UserWardrobe>> {
        return userWardrobeDao.getWardrobes().map { list -> list.map { it.toUserList() } }
    }

    override fun getWardrobeWithClothesById(id: Int): Flow<UserWardrobeWithClothes?> {
        Log.d("getError","inside repo by Id")
        return userWardrobeDao.getWardrobeWithClothesByUserId(id).map { it?.toUserListWithClothes() }
    }

    override suspend fun addWardrobeWithClothes(userWardrobeWithClothes: UserWardrobeWithClothes) {
        val userWardrobeEntity = UserWardrobeEntity(
            userId = userWardrobeWithClothes.userWardrobe.id ?: 0,
            username = userWardrobeWithClothes.userWardrobe.username,
            location = userWardrobeWithClothes.userWardrobe.location,
            lastUpdated = userWardrobeWithClothes.userWardrobe.lastUpdated
        )
        val id = userWardrobeDao.insertWardrobe(userWardrobeEntity).toInt()
        if (id != 0) {
            val clotheEntityList = userWardrobeWithClothes.listClothe.map {
               ClotheEntity(
                    clothId = it.id ?: 0,
                    clothe = it.clothe,
                    quantity = it.quantity,
                    typeId = it.typeId,
                    userWardrobeId = id
                )
            }
            clotheDao.insertClothes(clotheEntityList)
        }
    }

    override suspend fun updateWardrobeWithClothes(userWardrobeWithClothes: UserWardrobeWithClothes) {
        userWardrobeWithClothes.userWardrobe.id?.let { userId ->
            val userWardrobeEntity = UserWardrobeEntity(
                userId = userWardrobeWithClothes.userWardrobe.id,
                username = userWardrobeWithClothes.userWardrobe.username,
                location = userWardrobeWithClothes.userWardrobe.location,
                lastUpdated = userWardrobeWithClothes.userWardrobe.lastUpdated
            )
            userWardrobeDao.updateWardrobe(userWardrobeEntity)
            val clotheEntityList = userWardrobeWithClothes.listClothe.map {
                ClotheEntity(
                    clothId = it.id ?: 0,
                    clothe = it.clothe,
                    quantity = it.quantity,
                    typeId = it.typeId,
                    userWardrobeId = userId
                )
            }
            // get the currently stored clothes for this userList
            val currentlyStoredClothes = clotheDao.getClotheList(userId).firstOrNull()
            // if the list is not null we remove the elements that are no longer present in the current list
            currentlyStoredClothes?.let {
                val clothesToDelete = it.subtract(clotheEntityList.toSet()).toList()
                clotheDao.deleteClothes(clothesToDelete)
            }

            // we add the new list, if the cloth is already in the table it will be replaced with the new data
            clotheDao.insertClothes(clotheEntityList)
        }
    }

    override suspend fun deleteWardrobeWithClothes(userListId: Int) {
        clotheDao.deleteClothes(userListId)
        userWardrobeDao.deleteWardrobe(userListId)
    }
}