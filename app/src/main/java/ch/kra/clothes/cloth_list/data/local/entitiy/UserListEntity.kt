package ch.kra.clothes.cloth_list.data.local.entitiy

import androidx.room.Entity
import androidx.room.PrimaryKey
import ch.kra.clothes.cloth_list.domain.model.UserList
import java.util.*

@Entity
data class UserListEntity(
    @PrimaryKey(autoGenerate = true) val userId: Int = 0,
    val username: String,
    val location: String,
    val lastUpdated: Date
) {
    fun toUserList(): UserList {
        return UserList(
            id = userId,
            username = username,
            location = location,
            lastUpdated = lastUpdated
        )
    }
}
