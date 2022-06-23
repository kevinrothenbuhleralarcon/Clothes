package ch.kra.wardrobe.cloth_list.di

import ch.kra.wardrobe.cloth_list.domain.repository.WardrobeRepository
import ch.kra.wardrobe.cloth_list.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Provides
    @Singleton
    fun provideGetWardrobes(wardrobeRepository: WardrobeRepository): GetWardrobes {
        return GetWardrobes(wardrobeRepository)
    }

    @Provides
    @Singleton
    fun provideGetWardrobeWithClothesById(wardrobeRepository: WardrobeRepository): GetWardrobeWithClothesById {
        return GetWardrobeWithClothesById(wardrobeRepository)
    }

    @Provides
    @Singleton
    fun provideAddWardrobeWithClothes(wardrobeRepository: WardrobeRepository): AddWardrobeWithClothes {
        return AddWardrobeWithClothes(wardrobeRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateWardRobeWithClothes(wardrobeRepository: WardrobeRepository): UpdateWardrobeWithClothes {
        return UpdateWardrobeWithClothes(wardrobeRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteWardrobeWithClothes(wardrobeRepository: WardrobeRepository): DeleteWardrobeWithClothes {
        return DeleteWardrobeWithClothes(wardrobeRepository)
    }
}