package ch.kra.wardrobe.cloth_list.data.local.dao

import androidx.room.*
import ch.kra.wardrobe.cloth_list.data.local.entitiy.UserWardrobeEntity
import ch.kra.wardrobe.cloth_list.data.local.entitiy.UserWardrobeWithClothesPOJO
import kotlinx.coroutines.flow.Flow

@Dao
interface UserWardrobeDao {
    @Query("SELECT * FROM UserWardrobeEntity")
    fun getWardrobes(): Flow<List<UserWardrobeEntity>>

    @Transaction
    @Query("SELECT * FROM UserWardrobeEntity WHERE userId = :id")
    fun getWardrobeWithClothesByUserId(id: Int): Flow<UserWardrobeWithClothesPOJO>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWardrobe(userWardrobeEntity: UserWardrobeEntity): Long

    @Update
    suspend fun updateWardrobe(userWardrobeEntity: UserWardrobeEntity)

    @Query("DELETE FROM UserWardrobeEntity WHERE userId = :id")
    suspend fun deleteWardrobe(id: Int)
}