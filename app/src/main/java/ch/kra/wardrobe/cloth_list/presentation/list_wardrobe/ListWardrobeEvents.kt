package ch.kra.wardrobe.cloth_list.presentation.list_wardrobe

sealed class ListWardrobeEvents {
    object NewWardrobe: ListWardrobeEvents()
    data class EditWardrobe(val id: Int): ListWardrobeEvents()
}
