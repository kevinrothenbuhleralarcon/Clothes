package ch.kra.wardrobe.cloth_list.data.local.dao

import androidx.room.*
import ch.kra.wardrobe.cloth_list.data.local.entitiy.UserWardrobeEntity
import ch.kra.wardrobe.cloth_list.data.local.entitiy.UserWardrobeWithClothesPOJO
import kotlinx.coroutines.flow.Flow

@Dao
interface UserWardrobeDao {
    @Query("SELECT * FROM UserWardrobeEntity")
    fun getUsersList(): Flow<List<UserWardrobeEntity>>

    @Transaction
    @Query("SELECT * FROM UserWardrobeEntity WHERE userId = :id")
    fun getUserListWithClothesByUserId(id: Int): Flow<UserWardrobeWithClothesPOJO>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserList(userWardrobeEntity: UserWardrobeEntity): Long

    @Update
    suspend fun updateUserList(userWardrobeEntity: UserWardrobeEntity)

    @Query("DELETE FROM UserWardrobeEntity WHERE userId = :id")
    suspend fun deleteUserList(id: Int)
}