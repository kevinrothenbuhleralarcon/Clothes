package ch.kra.wardrobe.cloth_list.presentation.add_edit_wardrobe

import androidx.lifecycle.SavedStateHandle
import ch.kra.wardrobe.MainDispatcherRule
import ch.kra.wardrobe.cloth_list.core.TestDispatcher
import ch.kra.wardrobe.cloth_list.data.repository.FakeWardrobeRepositoryImpl
import ch.kra.wardrobe.cloth_list.domain.model.Clothe
import ch.kra.wardrobe.cloth_list.domain.model.UserWardrobe
import ch.kra.wardrobe.cloth_list.domain.model.UserWardrobeWithClothes
import ch.kra.wardrobe.cloth_list.domain.use_case.*
import ch.kra.wardrobe.core.Constants.NAVIGATION_WARDROBE_ID
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class AddEditWardrobeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var testDispatcher: TestDispatcher
    private lateinit var fakeRepository: FakeWardrobeRepositoryImpl
    private lateinit var viewModel: AddEditWardrobeViewModel

    @Before
    fun setUp() {
        testDispatcher = TestDispatcher()
        fakeRepository = FakeWardrobeRepositoryImpl()
    }

    private fun initViewModel(savedStateHandle: SavedStateHandle) {
        viewModel = AddEditWardrobeViewModel(
            GetWardrobeWithClothesById(fakeRepository),
            AddWardrobeWithClothes(fakeRepository),
            UpdateWardrobeWithClothes(fakeRepository),
            DeleteWardrobeWithClothes(fakeRepository),
            ValidateUsername(),
            ValidateLocation(),
            ValidateClothe(),
            ValidateQuantity(),
            testDispatcher,
            savedStateHandle
        )
    }

    @Test
    fun `init with no wardrobe id, wardrobeFormState should have default values`() = runTest {
        val id = -1
        val savedStateHandle = SavedStateHandle().apply {
            set(NAVIGATION_WARDROBE_ID, id)
        }
        initViewModel(savedStateHandle)
        val wardrobeFormState = viewModel.wardrobeFormState.value
        assertNull("Wardrobe id does not have the default value", wardrobeFormState.id)
        assertEquals("Username does not have the default value", "", wardrobeFormState.username)
        assertNull(
            "Username error does not have the default value",
            wardrobeFormState.usernameError
        )
        assertEquals("Location does not have the default value", "", wardrobeFormState.location)
        assertNull(
            "Location error does not have the default value",
            wardrobeFormState.locationError
        )
        assertEquals(
            "Clothe list does not have the default value",
            0,
            wardrobeFormState.clotheList.size
        )
    }

    @Test
    fun `init with a correct wardrobe id, wardrobeFormState should have the correct values`() =
        runTest {
            val testUserWardrobeWithClothes = UserWardrobeWithClothes(
                userWardrobe = UserWardrobe(
                    id = 50,
                    username = "test username",
                    location = "test location",
                    lastUpdated = Date(5000L)
                ),
                listClothe = listOf(
                    Clothe(id = 1, clothe = "T-shirt", quantity = 10, typeId = 1),
                    Clothe(id = 2, clothe = "Short", quantity = 3, typeId = 2)
                ).sortedWith(compareBy<Clothe> { it.typeId }.thenBy { it.clothe })
            )
            fakeRepository.listUserWardrobeWithClothes.add(testUserWardrobeWithClothes)
            val savedStateHandle = SavedStateHandle().apply {
                set(NAVIGATION_WARDROBE_ID, testUserWardrobeWithClothes.userWardrobe.id)
            }
            initViewModel(savedStateHandle)
            advanceUntilIdle()
            assertEquals(
                "Wardrobe id does not have the correct value",
                testUserWardrobeWithClothes.userWardrobe.id,
                viewModel.wardrobeFormState.value.id
            )
            assertEquals(
                "Username does not have the correct value",
                testUserWardrobeWithClothes.userWardrobe.username,
                viewModel.wardrobeFormState.value.username
            )
            assertNull(
                "Username error does not have the default value",
                viewModel.wardrobeFormState.value.usernameError
            )
            assertEquals(
                "Location does not have the correct value",
                testUserWardrobeWithClothes.userWardrobe.location,
                viewModel.wardrobeFormState.value.location
            )
            assertNull(
                "Location error does not have the default value",
                viewModel.wardrobeFormState.value.locationError
            )
            if (viewModel.wardrobeFormState.value.clotheList.size == testUserWardrobeWithClothes.listClothe.size) {
                for (i in 0 until testUserWardrobeWithClothes.listClothe.size) {
                    assertEquals(
                        "Clothe id is not correct",
                        testUserWardrobeWithClothes.listClothe[i].id,
                        viewModel.wardrobeFormState.value.clotheList[i].id
                    )
                    assertEquals(
                        "Clothe name is not correct",
                        testUserWardrobeWithClothes.listClothe[i].clothe,
                        viewModel.wardrobeFormState.value.clotheList[i].clothe
                    )
                    assertNull(
                        "Clothe name error is not the default value",
                        viewModel.wardrobeFormState.value.clotheList[i].clotheError
                    )
                    assertEquals(
                        "Quantity is not correct",
                        testUserWardrobeWithClothes.listClothe[i].quantity,
                        viewModel.wardrobeFormState.value.clotheList[i].quantity
                    )
                    assertNull(
                        "Quantity error is not have the default value",
                        viewModel.wardrobeFormState.value.clotheList[i].quantityError
                    )
                    assertEquals(
                        "The type is not correct",
                        testUserWardrobeWithClothes.listClothe[i].typeId,
                        viewModel.wardrobeFormState.value.clotheList[i].type
                    )
                }
            } else {
                fail("Clothe list does not have the correct size, Expected:<${testUserWardrobeWithClothes.listClothe.size}>, but was<${viewModel.wardrobeFormState.value.clotheList.size}>")
            }
        }

    @Test
    fun `onEvent with UsernameChanged, username is changed`() = runTest {
        val id = -1
        val savedStateHandle = SavedStateHandle().apply {
            set(NAVIGATION_WARDROBE_ID, id)
        }
        initViewModel(savedStateHandle)
        advanceUntilIdle()
        assertEquals("The username is not empty", "", viewModel.wardrobeFormState.value.username)

        val newUsername = "Test"
        viewModel.onEvent(AddEditWardrobeEvents.UsernameChanged(newUsername))
        assertEquals(
            "The username is not correct",
            newUsername,
            viewModel.wardrobeFormState.value.username
        )

        val updatedUsername = "Test 2"
        viewModel.onEvent(AddEditWardrobeEvents.UsernameChanged(updatedUsername))
        assertEquals(
            "The username is not correct",
            updatedUsername,
            viewModel.wardrobeFormState.value.username
        )
    }

    @Test
    fun `onEvent with LocationChanged, location is changed`() = runTest {
        val id = -1
        val savedStateHandle = SavedStateHandle().apply {
            set(NAVIGATION_WARDROBE_ID, id)
        }
        initViewModel(savedStateHandle)
        advanceUntilIdle()
        assertEquals("The location is not empty", "", viewModel.wardrobeFormState.value.location)

        val newLocation = "Test"
        viewModel.onEvent(AddEditWardrobeEvents.LocationChanged(newLocation))
        assertEquals(
            "The location is not correct",
            newLocation,
            viewModel.wardrobeFormState.value.location
        )

        val updatedLocation = "Test 2"
        viewModel.onEvent(AddEditWardrobeEvents.LocationChanged(updatedLocation))
        assertEquals(
            "The location is not correct",
            updatedLocation,
            viewModel.wardrobeFormState.value.location
        )
    }

    @Test
    fun `onEvent with ClotheChanged, currentClothe clothe is changed`() = runTest {
        val id = -1
        val savedStateHandle = SavedStateHandle().apply {
            set(NAVIGATION_WARDROBE_ID, id)
        }
        initViewModel(savedStateHandle)
        advanceUntilIdle()
        assertEquals("Clothe is not empty", "", viewModel.currentClothe.value.clothe)

        val newClothe = "test"
        viewModel.onEvent(AddEditWardrobeEvents.ClotheChanged(newClothe))
        assertEquals("Clothe is not changed", newClothe, viewModel.currentClothe.value.clothe)

        val updateClothe = "test2"
        viewModel.onEvent(AddEditWardrobeEvents.ClotheChanged(updateClothe))
        assertEquals("Clothe is not changed", updateClothe, viewModel.currentClothe.value.clothe)
    }

    @Test
    fun `onEvent with QuantityChanged, currentClothe quantity is changed`() = runTest {
        val id = -1
        val savedStateHandle = SavedStateHandle().apply {
            set(NAVIGATION_WARDROBE_ID, id)
        }
        initViewModel(savedStateHandle)
        advanceUntilIdle()
        assertNull("quantity is not null", viewModel.currentClothe.value.quantity)

        val newQuantity = 10
        viewModel.onEvent(AddEditWardrobeEvents.QuantityChanged(newQuantity))
        assertEquals("Quantity is not changed", newQuantity, viewModel.currentClothe.value.quantity)

        val updateQuantity = 50
        viewModel.onEvent(AddEditWardrobeEvents.QuantityChanged(updateQuantity))
        assertEquals(
            "Quantity is not changed",
            updateQuantity,
            viewModel.currentClothe.value.quantity
        )
    }

    @Test
    fun `onEvent with TypeChanged, currentClothe type is changed`() = runTest {
        val id = -1
        val savedStateHandle = SavedStateHandle().apply {
            set(NAVIGATION_WARDROBE_ID, id)
        }
        initViewModel(savedStateHandle)
        advanceUntilIdle()
        assertEquals("Type is not 0", 0, viewModel.currentClothe.value.type)

        val newType = 10
        viewModel.onEvent(AddEditWardrobeEvents.TypeChanged(newType))
        assertEquals("Type is not changed", newType, viewModel.currentClothe.value.type)

        val updateType = 50
        viewModel.onEvent(AddEditWardrobeEvents.TypeChanged(updateType))
        assertEquals("Type is not changed", updateType, viewModel.currentClothe.value.type)
    }

    @Test
    fun `onEvent with AddClothe, currentClothe is reset and the displayForm is set to true`() =
        runTest {
            val id = -1
            val savedStateHandle = SavedStateHandle().apply {
                set(NAVIGATION_WARDROBE_ID, id)
            }
            initViewModel(savedStateHandle)
            advanceUntilIdle()
            val clothe = "test"
            val quantity = 10
            val type = 2
            viewModel.onEvent(AddEditWardrobeEvents.ClotheChanged(clothe))
            viewModel.onEvent(AddEditWardrobeEvents.QuantityChanged(quantity))
            viewModel.onEvent(AddEditWardrobeEvents.TypeChanged(type))
            assertEquals("Clothe is not correct", clothe, viewModel.currentClothe.value.clothe)
            assertEquals(
                "Quantity is not correct",
                quantity,
                viewModel.currentClothe.value.quantity
            )
            assertEquals("Type is not correct", type, viewModel.currentClothe.value.type)
            assertFalse("displayClothe should be false", viewModel.displayClotheForm.value)

            viewModel.onEvent(AddEditWardrobeEvents.AddClothe)
            assertEquals("Clothe is not correct", "", viewModel.currentClothe.value.clothe)
            assertNull("Quantity is not correct", viewModel.currentClothe.value.quantity)
            assertEquals("Type is not correct", 0, viewModel.currentClothe.value.type)
            assertTrue("displayClothe should be true", viewModel.displayClotheForm.value)
        }

    @Test
    fun `onEvent with UpdateClothe, specified clothe is set to currentClothe and displayForm is set to true`() =
        runTest {
            val testUserWardrobeWithClothes = UserWardrobeWithClothes(
                userWardrobe = UserWardrobe(
                    id = 50,
                    username = "test username",
                    location = "test location",
                    lastUpdated = Date(5000L)
                ),
                listClothe = listOf(
                    Clothe(id = 1, clothe = "T-shirt", quantity = 10, typeId = 1),
                    Clothe(id = 2, clothe = "Short", quantity = 3, typeId = 2)
                ).sortedWith(compareBy<Clothe> { it.typeId }.thenBy { it.clothe })
            )
            fakeRepository.listUserWardrobeWithClothes.add(testUserWardrobeWithClothes)
            val savedStateHandle = SavedStateHandle().apply {
                set(NAVIGATION_WARDROBE_ID, testUserWardrobeWithClothes.userWardrobe.id)
            }
            initViewModel(savedStateHandle)
            advanceUntilIdle()
            assertEquals(
                "The second cloth is not correct",
                testUserWardrobeWithClothes.listClothe.last().clothe,
                viewModel.wardrobeFormState.value.clotheList.last().clothe
            )
            assertEquals("Clothe is not correct", "", viewModel.currentClothe.value.clothe)
            assertNull("Quantity is not correct", viewModel.currentClothe.value.quantity)
            assertEquals("Type is not correct", 0, viewModel.currentClothe.value.type)
            assertFalse("displayClothe should be false", viewModel.displayClotheForm.value)

            viewModel.onEvent(AddEditWardrobeEvents.UpdateClothe(1))
            assertEquals("Clothe is not correct", testUserWardrobeWithClothes.listClothe.last().clothe, viewModel.currentClothe.value.clothe)
            assertEquals("Quantity is not correct", testUserWardrobeWithClothes.listClothe.last().quantity, viewModel.currentClothe.value.quantity)
            assertEquals("Type is not correct", testUserWardrobeWithClothes.listClothe.last().typeId, viewModel.currentClothe.value.type)
            assertTrue("displayClothe should be true", viewModel.displayClotheForm.value)

        }

    @Test
    fun `onEvent with SaveClothe no clothe and negative quantity, the errors are set and the clothe is not saved`() = runTest {
        val id = -1
        val savedStateHandle = SavedStateHandle().apply {
            set(NAVIGATION_WARDROBE_ID, id)
        }
        initViewModel(savedStateHandle)
        advanceUntilIdle()
        assertNull("ClotheError is not null", viewModel.currentClothe.value.clotheError)
        assertNull("QuantityError is not null", viewModel.currentClothe.value.quantityError)
        assertEquals("ClotheList is not empty", 0, viewModel.wardrobeFormState.value.clotheList.size)

        viewModel.onEvent(AddEditWardrobeEvents.QuantityChanged(-2))
        viewModel.onEvent(AddEditWardrobeEvents.SaveClothe)
        assertNotNull("ClotheError is null", viewModel.currentClothe.value.clotheError)
        assertNotNull("QuantityError is null", viewModel.currentClothe.value.quantityError)
        assertEquals("ClotheList is not empty", 0, viewModel.wardrobeFormState.value.clotheList.size)
    }

    @Test
    fun `onEvent with SaveClothe and one of the data incorrect, the correct errors is set and the clothe is not saved`() = runTest {
        val id = -1
        val savedStateHandle = SavedStateHandle().apply {
            set(NAVIGATION_WARDROBE_ID, id)
        }
        initViewModel(savedStateHandle)
        advanceUntilIdle()
        assertNull("ClotheError is not null", viewModel.currentClothe.value.clotheError)
        assertNull("QuantityError is not null", viewModel.currentClothe.value.quantityError)
        assertEquals("ClotheList is not empty", 0, viewModel.wardrobeFormState.value.clotheList.size)

        viewModel.onEvent(AddEditWardrobeEvents.QuantityChanged(5))
        viewModel.onEvent(AddEditWardrobeEvents.SaveClothe)
        assertNotNull("ClotheError is null", viewModel.currentClothe.value.clotheError)
        assertNull("QuantityError is not null", viewModel.currentClothe.value.quantityError)
        assertEquals("ClotheList is not empty", 0, viewModel.wardrobeFormState.value.clotheList.size)

        viewModel.onEvent(AddEditWardrobeEvents.ClotheChanged("test"))
        viewModel.onEvent(AddEditWardrobeEvents.QuantityChanged(-2))
        viewModel.onEvent(AddEditWardrobeEvents.SaveClothe)
        assertNull("ClotheError is not null", viewModel.currentClothe.value.clotheError)
        assertNotNull("QuantityError is null", viewModel.currentClothe.value.quantityError)
        assertEquals("ClotheList is not empty", 0, viewModel.wardrobeFormState.value.clotheList.size)
    }

    @Test
    fun `onEvent with SaveClothe data are correct and it is a new clothe, the clothe is saved to the list`() = runTest {
        val id = -1
        val savedStateHandle = SavedStateHandle().apply {
            set(NAVIGATION_WARDROBE_ID, id)
        }
        initViewModel(savedStateHandle)
        advanceUntilIdle()
        assertEquals("ClotheList is not empty", 0, viewModel.wardrobeFormState.value.clotheList.size)

        viewModel.onEvent(AddEditWardrobeEvents.ClotheChanged("test"))
        viewModel.onEvent(AddEditWardrobeEvents.QuantityChanged(2))
        viewModel.onEvent(AddEditWardrobeEvents.SaveClothe)
        assertEquals("ClotheList is empty", 1, viewModel.wardrobeFormState.value.clotheList.size)
    }

    @Test
    fun `onEvent with SaveClothe data are correct and it is an update of a clothe, the clothe is correctly updated`() = runTest {
        val testUserWardrobeWithClothes = UserWardrobeWithClothes(
            userWardrobe = UserWardrobe(
                id = 50,
                username = "test username",
                location = "test location",
                lastUpdated = Date(5000L)
            ),
            listClothe = listOf(
                Clothe(id = 1, clothe = "T-shirt", quantity = 10, typeId = 1),
                Clothe(id = 2, clothe = "Short", quantity = 3, typeId = 2)
            ).sortedWith(compareBy<Clothe> { it.typeId }.thenBy { it.clothe })
        )
        fakeRepository.listUserWardrobeWithClothes.add(testUserWardrobeWithClothes)
        val savedStateHandle = SavedStateHandle().apply {
            set(NAVIGATION_WARDROBE_ID, testUserWardrobeWithClothes.userWardrobe.id)
        }
        initViewModel(savedStateHandle)
        advanceUntilIdle()
        assertEquals(
            "ClotheList is does not have the correct size",
            testUserWardrobeWithClothes.listClothe.size,
            viewModel.wardrobeFormState.value.clotheList.size
        )
        assertEquals(
            "The cloth name is not correct",
            testUserWardrobeWithClothes.listClothe[1].clothe,
            viewModel.wardrobeFormState.value.clotheList[1].clothe
        )

        val newClothe = "test"
        val newQuantity = 6
        val newType = 8

        viewModel.onEvent(AddEditWardrobeEvents.UpdateClothe(1))
        viewModel.onEvent(AddEditWardrobeEvents.ClotheChanged(newClothe))
        viewModel.onEvent(AddEditWardrobeEvents.QuantityChanged(newQuantity))
        viewModel.onEvent(AddEditWardrobeEvents.TypeChanged(newType))
        viewModel.onEvent(AddEditWardrobeEvents.SaveClothe)
        assertEquals("The clothe is not correct", newClothe, viewModel.wardrobeFormState.value.clotheList[1].clothe)
        assertEquals("The quantity is not correct", newQuantity, viewModel.wardrobeFormState.value.clotheList[1].quantity)
        assertEquals("The type is not correct", newType, viewModel.wardrobeFormState.value.clotheList[1].type)
    }

    @Test
    fun `onEvent with AddWardrobe, wardrobe is correctly added`() = runTest {
        val id = -1
        val savedStateHandle = SavedStateHandle().apply {
            set(NAVIGATION_WARDROBE_ID, id)
        }
        initViewModel(savedStateHandle)
        advanceUntilIdle()
        assertEquals("The list is not empty", 0, fakeRepository.listUserWardrobeWithClothes.size)
        val username = "test username"
        val location = "test location"
        val clothe1 = "T-shirt"
        val quantity1 = 10
        val type1 = 1
        viewModel.onEvent(AddEditWardrobeEvents.UsernameChanged(username))
        viewModel.onEvent(AddEditWardrobeEvents.LocationChanged(location))
        viewModel.onEvent(AddEditWardrobeEvents.AddClothe)
        viewModel.onEvent(AddEditWardrobeEvents.ClotheChanged(clothe1))
        viewModel.onEvent(AddEditWardrobeEvents.QuantityChanged(quantity1))
        viewModel.onEvent(AddEditWardrobeEvents.TypeChanged(type1))
        viewModel.onEvent(AddEditWardrobeEvents.SaveClothe)
        viewModel.onEvent(AddEditWardrobeEvents.SaveWardrobe)
        advanceUntilIdle()
        assertEquals(
            "The list is not the correct size",
            1,
            fakeRepository.listUserWardrobeWithClothes.size
        )
    }
}