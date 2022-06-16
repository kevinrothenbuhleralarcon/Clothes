package ch.kra.clothes.cloth_list.data.local.dao

import androidx.room.*
import ch.kra.clothes.cloth_list.data.local.entitiy.ClotheEntity
import ch.kra.clothes.cloth_list.domain.model.Clothe
import kotlinx.coroutines.flow.Flow

@Dao
interface ClotheDao {
    @Query("SELECT * FROM ClotheEntity WHERE userListId = :userId")
    fun getClotheList(userId: Int): Flow<List<ClotheEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertClothes(clothes: List<ClotheEntity>)

    @Update
    suspend fun updateClothes(clothes: List<ClotheEntity>)

    @Delete
    suspend fun deleteClothes(clothes: List<ClotheEntity>)
}