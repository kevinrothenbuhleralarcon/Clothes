package ch.kra.wardrobe.cloth_list.domain.use_case

import ch.kra.wardrobe.cloth_list.di.WardrobeModule
import ch.kra.wardrobe.cloth_list.domain.model.Clothe
import ch.kra.wardrobe.cloth_list.domain.model.UserWardrobe
import ch.kra.wardrobe.cloth_list.domain.model.UserWardrobeWithClothes
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
class UseCasesTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var getWardrobes: GetWardrobes

    @Inject
    lateinit var getWardrobeWithClothesById: GetWardrobeWithClothesById

    @Inject
    lateinit var addWardrobeWithClothes: AddWardrobeWithClothes

    @Inject
    lateinit var deleteWardrobeWithClothes: DeleteWardrobeWithClothes

    @Inject
    lateinit var updateWardrobeWithClothes: UpdateWardrobeWithClothes

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun testUseCases() = runBlocking {
        val newWardrobe1 = UserWardrobeWithClothes(
            userWardrobe = UserWardrobe(
                username = "User1",
                location = "Location 1",
                lastUpdated = Date()
            ),
            listClothe = listOf(
                Clothe(clothe = "T-Shirt", quantity = 5, typeId = 1),
                Clothe(clothe = "Jeans", quantity = 2, typeId = 2),
                Clothe(clothe = "Socks", quantity = 3, typeId = 3)
            )
        )
        val newWardrobe2 = UserWardrobeWithClothes(
            userWardrobe = UserWardrobe(
                username = "User2",
                location = "Location 2",
                lastUpdated = Date()
            ),
            listClothe = listOf(
                Clothe(clothe = "T-Shirt", quantity = 10, typeId = 1),
                Clothe(clothe = "Jeans", quantity = 6, typeId = 2),
                Clothe(clothe = "Socks", quantity = 10, typeId = 3),
                Clothe(clothe = "Pullover", quantity = 3, typeId = 1),
            )
        )

        addWardrobeWithClothes(newWardrobe1)
        addWardrobeWithClothes(newWardrobe2)

        val wardrobesFlow = getWardrobes()
        val wardrobes = wardrobesFlow.first()
        assertEquals("The number of wardrobes should be 2", 2, wardrobes.size)
        assertEquals("The first wardrobe should belong to: User1", "User1", wardrobes[0].username)
        assertEquals("The second wardrobe should belong to: User2", "User2", wardrobes[1].username)

        val user1WardrobeWithClothesFlow = getWardrobeWithClothesById(wardrobes[0].id!!)
        val user1WardrobeWithClothes = user1WardrobeWithClothesFlow.first()
        assertEquals("The wardrobe should belong to: User1", "User1", user1WardrobeWithClothes.userWardrobe.username)
        assertEquals("The wardrobe location should be: Location 1", "Location 1", user1WardrobeWithClothes.userWardrobe.location)
        assertEquals("The cloth list should contain 3 items", 3, user1WardrobeWithClothes.listClothe.size)
        assertEquals("The first item should be: T-Shirt", "T-Shirt", user1WardrobeWithClothes.listClothe[0].clothe)
        assertEquals("There should be 5 T-Shirt in the wardrobe", 5, user1WardrobeWithClothes.listClothe[0].quantity)

        val updatedUser1ClotheList = user1WardrobeWithClothes.listClothe.toMutableList()
        updatedUser1ClotheList[0] = updatedUser1ClotheList[0].copy(quantity = 6)

        val updatedUser1Wardrobe = user1WardrobeWithClothes.copy(
            userWardrobe = user1WardrobeWithClothes.userWardrobe.copy(
                location = "New location 1"
            ),
            listClothe = updatedUser1ClotheList
        )
        updateWardrobeWithClothes(updatedUser1Wardrobe)

        val user1UpdatedWardrobeWithClothesFlow = getWardrobeWithClothesById(wardrobes[0].id!!)
        val user1UpdatedWardrobeWithClothes = user1UpdatedWardrobeWithClothesFlow.first()
        assertEquals("The wardrobe should belong to: User1", "User1", user1UpdatedWardrobeWithClothes.userWardrobe.username)
        assertEquals("The wardrobe location should be: New location 1", "New location 1", user1UpdatedWardrobeWithClothes.userWardrobe.location)
        assertEquals("The cloth list should contain 3 items", 3, user1UpdatedWardrobeWithClothes.listClothe.size)
        assertEquals("The first item should be: T-Shirt", "T-Shirt", user1UpdatedWardrobeWithClothes.listClothe[0].clothe)
        assertEquals("There should be 6 T-Shirt in the wardrobe", 6, user1UpdatedWardrobeWithClothes.listClothe[0].quantity)

        deleteWardrobeWithClothes(wardrobes[0].id!!)

        val wardrobesAfterDeletionFlow = getWardrobes()
        val wardrobesAfterDeletion = wardrobesAfterDeletionFlow.first()
        assertEquals("The number of wardrobes should be 1", 1, wardrobesAfterDeletion.size)
    }
}