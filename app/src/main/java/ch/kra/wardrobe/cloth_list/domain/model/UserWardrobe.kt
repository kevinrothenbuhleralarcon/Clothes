package ch.kra.wardrobe.cloth_list.domain.model

import java.util.*

data class UserWardrobe(
    val id: Int? = null,
    val username: String,
    val location: String,
    val lastUpdated: Date
)