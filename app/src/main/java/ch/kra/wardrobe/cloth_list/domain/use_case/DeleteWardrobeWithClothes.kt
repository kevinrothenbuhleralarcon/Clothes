package ch.kra.wardrobe.cloth_list.domain.use_case

import ch.kra.wardrobe.cloth_list.domain.repository.WardrobeRepository

class DeleteWardrobeWithClothes(private val wardrobeRepository: WardrobeRepository) {
    suspend operator fun invoke(id: Int) {
        wardrobeRepository.deleteWardrobeWithClothes(id)
    }
}