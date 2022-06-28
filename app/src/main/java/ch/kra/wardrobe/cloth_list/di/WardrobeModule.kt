package ch.kra.wardrobe.cloth_list.di

import android.app.Application
import androidx.room.Room
import ch.kra.wardrobe.cloth_list.data.local.WardrobeDatabase
import ch.kra.wardrobe.cloth_list.data.repository.WardrobeRepositoryImpl
import ch.kra.wardrobe.cloth_list.domain.repository.WardrobeRepository
import ch.kra.wardrobe.core.DefaultDispatcher
import ch.kra.wardrobe.core.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WardrobeModule {

    @Provides
    @Singleton
    fun provideClotheListDb(app: Application): WardrobeDatabase {
        return Room.databaseBuilder(
            app,
            WardrobeDatabase::class.java,
            "wardrobe_database")
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

    @Provides
    @Singleton
    fun provideDefaultDispatcher(): DispatcherProvider {
        return DefaultDispatcher()
    }
}