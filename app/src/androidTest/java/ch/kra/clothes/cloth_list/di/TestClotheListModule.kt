package ch.kra.clothes.cloth_list.di

import android.app.Application
import androidx.room.Room
import ch.kra.clothes.cloth_list.data.local.ClotheDatabase
import ch.kra.clothes.cloth_list.data.repository.ClotheRepositoryImpl
import ch.kra.clothes.cloth_list.domain.repository.ClotheRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestClotheListModule {

    @Provides
    @Singleton
    fun provideClotheListDb(app: Application): ClotheDatabase {
        return Room.inMemoryDatabaseBuilder(
            app,
            ClotheDatabase::class.java)
            .build()
    }

    @Provides
    @Singleton
    fun provideClotheRepository(db: ClotheDatabase): ClotheRepository {
        return ClotheRepositoryImpl(
            clotheDao = db.clotheDao,
            userListDao = db.userListDao
        )
    }
}