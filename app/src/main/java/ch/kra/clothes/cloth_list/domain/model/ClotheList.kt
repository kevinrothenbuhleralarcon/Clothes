package ch.kra.clothes.cloth_list.domain.model

import java.util.*

data class ClotheList(
    val listId: Int? = null,
    val username: String,
    val location: String,
    val lastUpdated: Date,
)
