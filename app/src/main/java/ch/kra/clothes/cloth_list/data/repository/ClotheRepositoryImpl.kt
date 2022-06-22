package ch.kra.clothes.cloth_list.data.repository

import android.util.Log
import ch.kra.clothes.cloth_list.data.local.dao.ClotheDao
import ch.kra.clothes.cloth_list.data.local.dao.UserListDao
import ch.kra.clothes.cloth_list.data.local.entitiy.ClotheEntity
import ch.kra.clothes.cloth_list.data.local.entitiy.UserListEntity
import ch.kra.clothes.cloth_list.domain.model.UserList
import ch.kra.clothes.cloth_list.domain.model.UserListWithClothes
import ch.kra.clothes.cloth_list.domain.repository.ClotheRepository
import kotlinx.coroutines.flow.*

class ClotheRepositoryImpl(
    private val clotheDao: ClotheDao,
    private val userListDao: UserListDao
): ClotheRepository {
    override fun getUsersList(): Flow<List<UserList>> {
        return userListDao.getUsersList().map { list -> list.map { it.toUserList() } }
    }

    override fun getUserListWithClothById(id: Int): Flow<UserListWithClothes> {
        return userListDao.getUserListWithClothesByUserId(id).map { it.toUserListWithClothes() }
    }

    override suspend fun addUserListWithClothes(userListWithClothes: UserListWithClothes) {
        val userListEntity = UserListEntity(
            userId = userListWithClothes.userList.id ?: 0,
            username = userListWithClothes.userList.username,
            location = userListWithClothes.userList.location,
            lastUpdated = userListWithClothes.userList.lastUpdated
        )
        val id = userListDao.insertUserList(userListEntity).toInt()
        if (id != 0) {
            val clotheEntityList = userListWithClothes.listClothe.map {
               ClotheEntity(
                    clothId = it.id ?: 0,
                    clothe = it.clothe,
                    quantity = it.quantity,
                    typeId = it.typeId,
                    userListId = id
                )
            }
            clotheDao.insertClothes(clotheEntityList)
        }
    }

    override suspend fun updateUserListWithClothes(userListWithClothes: UserListWithClothes) {
        userListWithClothes.userList.id?.let { userId ->
            val userListEntity = UserListEntity(
                userId = userListWithClothes.userList.id,
                username = userListWithClothes.userList.username,
                location = userListWithClothes.userList.location,
                lastUpdated = userListWithClothes.userList.lastUpdated
            )
            userListDao.updateUserList(userListEntity)
            val clotheEntityList = userListWithClothes.listClothe.map {
                ClotheEntity(
                    clothId = it.id ?: 0,
                    clothe = it.clothe,
                    quantity = it.quantity,
                    typeId = it.typeId,
                    userListId = userId
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

    override suspend fun deleteUserListWithClothes(userListId: Int) {
        clotheDao.deleteClothes(userListId)
        userListDao.deleteUserList(userListId)
    }
}