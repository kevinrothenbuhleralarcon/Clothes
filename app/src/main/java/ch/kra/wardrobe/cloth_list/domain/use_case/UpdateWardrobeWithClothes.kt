package ch.kra.wardrobe.cloth_list.domain.use_case

import ch.kra.wardrobe.cloth_list.domain.model.UserWardrobeWithClothes
import ch.kra.wardrobe.cloth_list.domain.repository.WardrobeRepository

class UpdateWardrobeWithClothes(private val wardrobeRepository: WardrobeRepository) {
    suspend operator fun invoke(userWardrobeWithClothes: UserWardrobeWithClothes) {
        wardrobeRepository.updateWardrobeWithClothes(userWardrobeWithClothes)
    }
}