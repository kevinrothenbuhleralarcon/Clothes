package ch.kra.wardrobe.cloth_list.domain.use_case

import ch.kra.wardrobe.cloth_list.domain.model.UserWardrobe
import ch.kra.wardrobe.cloth_list.domain.repository.WardrobeRepository
import kotlinx.coroutines.flow.Flow

class GetWardrobes(private val wardrobeRepository: WardrobeRepository) {
    operator fun invoke(): Flow<List<UserWardrobe>> {
        return wardrobeRepository.getWardrobes()
    }
}