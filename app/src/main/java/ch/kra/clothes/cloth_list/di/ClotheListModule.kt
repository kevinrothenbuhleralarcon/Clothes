package ch.kra.clothes.cloth_list.di

import android.app.Application
import androidx.room.Room
import ch.kra.clothes.cloth_list.data.local.ClotheDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ClotheListModule {

    @Provides
    @Singleton
    fun provideClotheListDb(app: Application): ClotheDatabase {
        return Room.databaseBuilder(
            app,
            ClotheDatabase::class.java,
            "clothe_database")
            .build()
    }
}