package ch.kra.wardrobe.cloth_list.di

import android.app.Application
import androidx.room.Room
import ch.kra.wardrobe.cloth_list.data.local.WardrobeDatabase
import ch.kra.wardrobe.cloth_list.data.repository.WardrobeRepositoryImpl
import ch.kra.wardrobe.cloth_list.domain.repository.WardrobeRepository
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
    fun provideClotheListDb(app: Application): WardrobeDatabase {
        return Room.inMemoryDatabaseBuilder(
            app,
            WardrobeDatabase::class.java)
            .build()
    }

    @Provides
    @Singleton
    fun provideClotheRepository(db: WardrobeDatabase): WardrobeRepository {
        return WardrobeRepositoryImpl(
            clotheDao = db.clotheDao,
            userWardrobeDao = db.userWardrobeDao
        )
    }
}