package ch.kra.wardrobe.cloth_list.presentation.add_edit_wardrobe

import ch.kra.wardrobe.core.AlertDialogSelection
import ch.kra.wardrobe.core.ClotheType

sealed class AddEditWardrobeEvents {
    data class UsernameChanged(val username: String): AddEditWardrobeEvents()
    data class LocationChanged(val location: String): AddEditWardrobeEvents()
    data class ClotheChanged(val clothe: String): AddEditWardrobeEvents()
    data class QuantityChanged(val quantity: Int?): AddEditWardrobeEvents()
    data class TypeChanged(val clotheType: ClotheType): AddEditWardrobeEvents()
    object SaveClothe: AddEditWardrobeEvents()
    object DeleteClothe: AddEditWardrobeEvents()
    object CancelClothe: AddEditWardrobeEvents()
    object AddClothe: AddEditWardrobeEvents()
    data class UpdateClothe(val id: Int): AddEditWardrobeEvents()
    object SaveWardrobe: AddEditWardrobeEvents()
    data class DeleteWardrobe(val dialogSelection: AlertDialogSelection? = null): AddEditWardrobeEvents()
    data class NavigateBackPressed(val dialogSelection: AlertDialogSelection? = null): AddEditWardrobeEvents()
}
