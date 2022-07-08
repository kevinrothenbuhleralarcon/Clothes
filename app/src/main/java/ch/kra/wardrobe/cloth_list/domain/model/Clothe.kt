package ch.kra.wardrobe.cloth_list.domain.model

import ch.kra.wardrobe.core.ClotheType

data class Clothe(
    val id: Int? = null,
    val clothe: String,
    val quantity: Int,
    val type: ClotheType,
)
