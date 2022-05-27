package ch.kra.clothes.cloth_list.data.local.dao

import androidx.room.*
import ch.kra.clothes.cloth_list.data.local.entitiy.UserListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserListDao {
    @Query("SELECT * FROM tbl_user_list")
    fun getUsersList(): Flow<List<UserListEntity>>

    @Query("SELECT * FROM tbl_user_list WHERE id = :id")
    fun getUserListById(id: Int): Flow<UserListEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserList(userListEntity: UserListEntity)

    @Update
    suspend fun updateUserList(userListEntity: UserListEntity)

    @Query("DELETE FROM tbl_user_list WHERE id = :id")
    suspend fun deleteUserList(id: Int)
}