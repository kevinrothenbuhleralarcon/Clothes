package ch.kra.clothes.cloth_list.domain.model

data class Clothe(
    val id: Int? = null,
    val clothe: String,
    val quantity: Int,
    val typeId: Int,
    val userListId: Int? = null
)
