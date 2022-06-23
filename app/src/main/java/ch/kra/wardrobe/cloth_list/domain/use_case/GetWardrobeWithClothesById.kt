package ch.kra.wardrobe.cloth_list.domain.use_case

import ch.kra.wardrobe.cloth_list.domain.model.UserWardrobeWithClothes
import ch.kra.wardrobe.cloth_list.domain.repository.WardrobeRepository
import kotlinx.coroutines.flow.Flow

class GetWardrobeWithClothesById(private val wardrobeRepository: WardrobeRepository) {
    operator fun invoke(id: Int): Flow<UserWardrobeWithClothes> {
        return wardrobeRepository.getWardrobeWithClothesById(id)
    }
}