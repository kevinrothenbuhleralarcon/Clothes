package ch.kra.wardrobe.cloth_list.presentation.add_edit_wardrobe

import ch.kra.wardrobe.core.UIText

data class WardrobeFormState(
    val id: Int? = null,
    val username: String = "",
    val usernameError: UIText? = null,
    val location: String = "",
    val locationError: UIText? = null,
    val clotheList: List<ClotheFormState> = emptyList()
)

data class ClotheFormState(
    val id: Int? = null,
    val clothe: String = "",
    val clotheError: UIText? = null,
    val quantity: Int? = null,
    val quantityError: UIText? = null,
    val type: Int = 0,
    val update: Boolean = false
)
