package ch.kra.wardrobe.cloth_list.data.repository

import ch.kra.wardrobe.cloth_list.di.WardrobeModule
import ch.kra.wardrobe.cloth_list.domain.model.Clothe
import ch.kra.wardrobe.cloth_list.domain.model.UserWardrobe
import ch.kra.wardrobe.cloth_list.domain.model.UserWardrobeWithClothes
import ch.kra.wardrobe.cloth_list.domain.repository.WardrobeRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(WardrobeModule::class)
class WardrobeRepositoryImplTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var wardrobeRepository: WardrobeRepository

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun testInsertUserListRetrieveItAndRemoveIt() = runBlocking {
        val newUserWardrobe = UserWardrobe(
            username = "test",
            location = "Lausanne",
            lastUpdated = Date()
        )
        val userWardrobeWithClothes = UserWardrobeWithClothes(
            userWardrobe = newUserWardrobe,
            listClothe = emptyList()
        )
        wardrobeRepository.addUserListWithClothes(userWardrobeWithClothes)

        val flowUserListAfterInsert = wardrobeRepository.getUsersList()
        val userListAfterInsert = flowUserListAfterInsert.first()
        assertEquals("The list should contain 1 item", 1, userListAfterInsert.size)
        assertEquals("The username is not correct", "test", userListAfterInsert[0].username)

        wardrobeRepository.deleteUserListWithClothes(userListAfterInsert[0].id!!)

        val flowUserListAfterDelete = wardrobeRepository.getUsersList()
        val userListAfterDelete = flowUserListAfterDelete.first()

        assertEquals("The list should contain 0 item", 0, userListAfterDelete.size)
    }

    @Test
    fun testInsertUserWithClothesRetrieveItUpdateItAndRemoveIt() = runBlocking {
        // Insert initial values
        val newUserWardrobe = UserWardrobe(
            username = "test",
            location = "Lausanne",
            lastUpdated = Date()
        )
        val newClotheList = listOf(
            Clothe(clothe = "T-shirt", quantity = 5, typeId = 6),
            Clothe(clothe = "Jeans", quantity = 2, typeId = 4),
            Clothe(clothe = "Short", quantity = 3, typeId = 4),
            Clothe(clothe = "Pullover", quantity = 1, typeId = 7)
        )
        val userWardrobeWithClothes = UserWardrobeWithClothes(
            userWardrobe = newUserWardrobe,
            listClothe = newClotheList
        )
        wardrobeRepository.addUserListWithClothes(userWardrobeWithClothes)

        // Test the insert and get
        val flowUserListAfterInsert = wardrobeRepository.getUsersList()
        val userListAfterInsert = flowUserListAfterInsert.first()
        assertEquals("The list should contain 1 item", 1, userListAfterInsert.size)
        assertEquals("The username is not correct", "test", userListAfterInsert[0].username)

        val flowUserWithClotheAfterInsert = wardrobeRepository.getUserListWithClothById(userListAfterInsert[0].id!!)
        val userWithClothesAfterInsert = flowUserWithClotheAfterInsert.first()
        assertEquals("The clothe list should contain 4 items", 4, userWithClothesAfterInsert.listClothe.size)
        assertEquals("The first clothe should be T-shirt", "T-shirt", userWithClothesAfterInsert.listClothe[0].clothe)
        assertEquals("The second clothe should be Jeans", "Jeans", userWithClothesAfterInsert.listClothe[1].clothe)
        assertEquals("The third clothe should be Short", "Short", userWithClothesAfterInsert.listClothe[2].clothe)
        assertEquals("The forth clothe should be Pullover", "Pullover", userWithClothesAfterInsert.listClothe[3].clothe)
        assertEquals("There should be 5 T-shirt", 5, userWithClothesAfterInsert.listClothe[0].quantity)

        // Update
        val updateUserList = newUserWardrobe.copy(id = userListAfterInsert[0].id, username = "Kevin", location = "Belmont-sur-Lausanne")
        val updateClotheList = userWithClothesAfterInsert.listClothe.toMutableList()
        updateClotheList[0] = updateClotheList[0].copy(quantity = 6)
        updateClotheList.add(Clothe(clothe = "Socks", quantity = 20, typeId = 8))
        updateClotheList.removeAt(2)
        val updateUserWardrobeWithClothes = UserWardrobeWithClothes(
            userWardrobe = updateUserList,
            listClothe = updateClotheList
        )
        wardrobeRepository.updateUserListWithClothes(updateUserWardrobeWithClothes)

        // Test the update
        val flowUserWithClotheAfterUpdate = wardrobeRepository.getUserListWithClothById(userListAfterInsert[0].id!!)
        val userWithClotheAfterUpdate = flowUserWithClotheAfterUpdate.first()
        assertEquals("The clothe list should contain 4 items", 4, userWithClotheAfterUpdate.listClothe.size)
        assertEquals("The first clothe should be T-shirt", "T-shirt", userWithClotheAfterUpdate.listClothe[0].clothe)
        assertEquals("The second clothe should be Jeans", "Jeans", userWithClotheAfterUpdate.listClothe[1].clothe)
        assertEquals("The third clothe should be Pullover", "Pullover", userWithClotheAfterUpdate.listClothe[2].clothe)
        assertEquals("The forth clothe should be Socks", "Socks", userWithClotheAfterUpdate.listClothe[3].clothe)
        assertEquals("There should be 6 T-shirt", 6, userWithClotheAfterUpdate.listClothe[0].quantity)
        assertEquals("There should be 20 Sock", 20, userWithClotheAfterUpdate.listClothe[3].quantity)

        // Delete
        wardrobeRepository.deleteUserListWithClothes(userListAfterInsert[0].id!!)

        // Test the delete
        val flowUserListAfterDelete = wardrobeRepository.getUsersList()
        val userListAfterDelete = flowUserListAfterDelete.first()

        assertEquals("The list should contain 0 item", 0, userListAfterDelete.size)
    }
}