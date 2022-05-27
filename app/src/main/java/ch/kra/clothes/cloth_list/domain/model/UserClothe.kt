package ch.kra.clothes.cloth_list.domain.model

import java.util.*

data class UserClothe(
    val id: Int = 0,
    val username: String,
    val location: String,
    val lastUpdated: Date
)