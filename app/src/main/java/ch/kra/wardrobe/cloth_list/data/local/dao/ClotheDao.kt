package ch.kra.wardrobe.cloth_list.data.local.dao

import androidx.room.*
import ch.kra.wardrobe.cloth_list.data.local.entitiy.ClotheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClotheDao {
    @Query("SELECT * FROM ClotheEntity WHERE userWardrobeId = :userId")
    fun getClotheList(userId: Int): Flow<List<ClotheEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClothes(clothes: List<ClotheEntity>): List<Long>

    @Delete
    suspend fun deleteClothes(clothes: List<ClotheEntity>)

    @Query("DELETE FROM ClotheEntity WHERE userWardrobeId = :userId")
    suspend fun deleteClothes(userId: Int)
}