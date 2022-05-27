package ch.kra.clothes.cloth_list.data.local.dao

import androidx.room.*
import ch.kra.clothes.cloth_list.data.local.entitiy.ClotheTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClothTypeDao {
    @Query("SELECT * FROM tbl_clothe_type")
    fun getClotheTypes(): Flow<List<ClotheTypeEntity>>

    @Query("SELECT * FROM tbl_clothe_type WHERE id = :id")
    fun getClotheType(id: Int): Flow<ClotheTypeEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertClotheType(clotheTypeEntity: ClotheTypeEntity)

    @Update
    suspend fun updateClotheType(clotheTypeEntity: ClotheTypeEntity)

    @Delete
    suspend fun deleteClotheType(clotheTypeEntity: ClotheTypeEntity)
}