package ch.kra.wardrobe.cloth_list.presentation.add_edit_wardrobe

import ch.kra.wardrobe.cloth_list.domain.model.Clothe
import ch.kra.wardrobe.core.ClotheType
import ch.kra.wardrobe.core.UIText

data class WardrobeFormState(
    val id: Int? = null,
    val username: String = "",
    val usernameError: UIText? = null,
    val location: String = "",
    val locationError: UIText? = null,

    val clothesByType: Map<ClotheType, ClotheListState> = emptyMap(),

    val currentClothe: ClotheFormState = ClotheFormState(),

    val displayBackDialog: Boolean = false,
    val displayDeleteDialog: Boolean = false
)

data class ClotheFormState(
    val id: Int? = null,
    val clothe: String = "",
    val clotheError: UIText? = null,
    val quantity: Int? = null,
    val quantityError: UIText? = null,
    val type: ClotheType = ClotheType.UPPER_BODY,
    val originalType: ClotheType? = null,
    val displayClotheForm: Boolean = false,
)

data class ClotheListState(
    val isExpanded: Boolean = false,
    val clotheList: List<Clothe>
)
