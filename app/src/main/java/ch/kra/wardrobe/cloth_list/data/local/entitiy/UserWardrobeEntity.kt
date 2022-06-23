package ch.kra.wardrobe.cloth_list.data.local.entitiy

import androidx.room.Entity
import androidx.room.PrimaryKey
import ch.kra.wardrobe.cloth_list.domain.model.UserWardrobe
import java.util.*

@Entity
data class UserWardrobeEntity(
    @PrimaryKey(autoGenerate = true) val userId: Int = 0,
    val username: String,
    val location: String,
    val lastUpdated: Date
) {
    fun toUserList(): UserWardrobe {
        return UserWardrobe(
            id = userId,
            username = username,
            location = location,
            lastUpdated = lastUpdated
        )
    }
}
