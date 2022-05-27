package ch.kra.clothes.cloth_list.domain.model

import java.util.*

data class UserClothe(
    val id: Int? = null,
    val username: String,
    val location: String,
    val lastUpdated: Date
)