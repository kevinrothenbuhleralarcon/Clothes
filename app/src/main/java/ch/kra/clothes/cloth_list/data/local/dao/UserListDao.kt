package ch.kra.clothes.cloth_list.data.local.dao

import androidx.room.*
import ch.kra.clothes.cloth_list.data.local.entitiy.UserListEntity
import ch.kra.clothes.cloth_list.data.local.entitiy.UserListWithClothesPOJO
import kotlinx.coroutines.flow.Flow

@Dao
interface UserListDao {
    @Query("SELECT * FROM UserListEntity")
    fun getUsersList(): Flow<List<UserListEntity>>

    @Transaction
    @Query("SELECT * FROM UserListEntity WHERE userId = :id")
    fun getUserListWithClothesByUserId(id: Int): Flow<UserListWithClothesPOJO>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserList(userListEntity: UserListEntity): Long

    @Update
    suspend fun updateUserList(userListEntity: UserListEntity)

    @Query("DELETE FROM UserListEntity WHERE userId = :id")
    suspend fun deleteUserList(id: Int)
}