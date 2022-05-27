package ch.kra.clothes.cloth_list.data.local.dao

import androidx.room.*
import ch.kra.clothes.cloth_list.data.local.entitiy.ClotheListEntity
import ch.kra.clothes.cloth_list.domain.model.ClotheList
import kotlinx.coroutines.flow.Flow

@Dao
interface ClotheListDao {
    @Query("SELECT * FROM tbl_clothe_list WHERE user_list_id = :userId")
    fun getClotheList(userId: Int): Flow<List<ClotheList>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertClothes(clothes: List<ClotheListEntity>)

    @Update
    suspend fun updateClothes(clothes: List<ClotheListEntity>)

    @Delete
    suspend fun deleteClothes(clothes: List<ClotheListEntity>)
}