package ch.kra.wardrobe.cloth_list.domain.model

data class UserWardrobeWithClothes(
    val userWardrobe: UserWardrobe,
    val listClothe: List<Clothe>
)
