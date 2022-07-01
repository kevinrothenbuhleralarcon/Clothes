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
        wardrobeRepository.addWardrobeWithClothes(userWardrobeWithClothes)

        val flowUserListAfterInsert = wardrobeRepository.getWardrobes()
        val userListAfterInsert = flowUserListAfterInsert.first()
        assertEquals("The list should contain 1 item", 1, userListAfterInsert.size)
        assertEquals("The username is not correct", "test", userListAfterInsert[0].username)

        wardrobeRepository.deleteWardrobeWithClothes(userListAfterInsert[0].id!!)

        val flowUserListAfterDelete = wardrobeRepository.getWardrobes()
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
        wardrobeRepository.addWardrobeWithClothes(userWardrobeWithClothes)

        // Test the insert and get
        val flowUserListAfterInsert = wardrobeRepository.getWardrobes()
        val userListAfterInsert = flowUserListAfterInsert.first()
        assertEquals("The list should contain 1 item", 1, userListAfterInsert.size)
        assertEquals("The username is not correct", "test", userListAfterInsert[0].username)

        val flowUserWithClotheAfterInsert = wardrobeRepository.getWardrobeWithClothesById(userListAfterInsert[0].id!!)
        val userWithClothesAfterInsert = flowUserWithClotheAfterInsert.first()
        userWithClothesAfterInsert?.let{
            assertEquals("The clothe list should contain 4 items", 4, it.listClothe.size)
            assertEquals("The first clothe should be T-shirt", "T-shirt", it.listClothe[0].clothe)
            assertEquals("The second clothe should be Jeans", "Jeans", it.listClothe[1].clothe)
            assertEquals("The third clothe should be Short", "Short", it.listClothe[2].clothe)
            assertEquals("The forth clothe should be Pullover", "Pullover", it.listClothe[3].clothe)
            assertEquals("There should be 5 T-shirt", 5, it.listClothe[0].quantity)
        } ?: fail()


        // Update
        val updateUserList = newUserWardrobe.copy(id = userListAfterInsert[0].id, username = "Kevin", location = "Belmont-sur-Lausanne")
        val updateClotheList = userWithClothesAfterInsert!!.listClothe.toMutableList()
        updateClotheList[0] = updateClotheList[0].copy(quantity = 6)
        updateClotheList.add(Clothe(clothe = "Socks", quantity = 20, typeId = 8))
        updateClotheList.removeAt(2)
        val updateUserWardrobeWithClothes = UserWardrobeWithClothes(
            userWardrobe = updateUserList,
            listClothe = updateClotheList
        )
        wardrobeRepository.updateWardrobeWithClothes(updateUserWardrobeWithClothes)

        // Test the update
        val flowUserWithClotheAfterUpdate = wardrobeRepository.getWardrobeWithClothesById(userListAfterInsert[0].id!!)
        val userWithClotheAfterUpdate = flowUserWithClotheAfterUpdate.first()
        userWithClotheAfterUpdate?.let {
            assertEquals("The clothe list should contain 4 items", 4, it.listClothe.size)
            assertEquals("The first clothe should be T-shirt", "T-shirt", it.listClothe[0].clothe)
            assertEquals("The second clothe should be Jeans", "Jeans", it.listClothe[1].clothe)
            assertEquals("The third clothe should be Pullover", "Pullover", it.listClothe[2].clothe)
            assertEquals("The forth clothe should be Socks", "Socks", it.listClothe[3].clothe)
            assertEquals("There should be 6 T-shirt", 6, it.listClothe[0].quantity)
            assertEquals("There should be 20 Sock", 20, it.listClothe[3].quantity)
        }


        // Delete
        wardrobeRepository.deleteWardrobeWithClothes(userListAfterInsert[0].id!!)

        // Test the delete
        val flowUserListAfterDelete = wardrobeRepository.getWardrobes()
        val userListAfterDelete = flowUserListAfterDelete.first()

        assertEquals("The list should contain 0 item", 0, userListAfterDelete.size)
    }
}