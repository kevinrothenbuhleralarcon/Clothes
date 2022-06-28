package ch.kra.wardrobe.cloth_list.presentation.add_edit_wardrobe

import androidx.lifecycle.SavedStateHandle
import ch.kra.wardrobe.MainDispatcherRule
import ch.kra.wardrobe.cloth_list.core.TestDispatcher
import ch.kra.wardrobe.cloth_list.data.repository.FakeWardrobeRepositoryImpl
import ch.kra.wardrobe.cloth_list.domain.model.Clothe
import ch.kra.wardrobe.cloth_list.domain.model.UserWardrobe
import ch.kra.wardrobe.cloth_list.domain.model.UserWardrobeWithClothes
import ch.kra.wardrobe.cloth_list.domain.repository.WardrobeRepository
import ch.kra.wardrobe.cloth_list.domain.use_case.*
import ch.kra.wardrobe.core.Constants.NAVIGATION_WARDROBE_ID
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
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
            val wardrobeFormState = viewModel.wardrobeFormState.value
            assertEquals(
                "Wardrobe id does not have the correct value",
                testUserWardrobeWithClothes.userWardrobe.id,
                wardrobeFormState.id
            )
            assertEquals(
                "Username does not have the correct value",
                testUserWardrobeWithClothes.userWardrobe.username,
                wardrobeFormState.username
            )
            assertNull(
                "Username error does not have the default value",
                wardrobeFormState.usernameError
            )
            assertEquals(
                "Location does not have the correct value",
                testUserWardrobeWithClothes.userWardrobe.location,
                wardrobeFormState.location
            )
            assertNull(
                "Location error does not have the default value",
                wardrobeFormState.locationError
            )
            if (wardrobeFormState.clotheList.size == testUserWardrobeWithClothes.listClothe.size) {
                for (i in 0 until testUserWardrobeWithClothes.listClothe.size) {
                    assertEquals(
                        "Clothe id is not correct",
                        testUserWardrobeWithClothes.listClothe[i].id,
                        wardrobeFormState.clotheList[i].id
                    )
                    assertEquals(
                        "Clothe name is not correct",
                        testUserWardrobeWithClothes.listClothe[i].clothe,
                        wardrobeFormState.clotheList[i].clothe
                    )
                    assertNull(
                        "Clothe name error is not the default value",
                        wardrobeFormState.clotheList[i].clotheError
                    )
                    assertEquals(
                        "Quantity is not correct",
                        testUserWardrobeWithClothes.listClothe[i].quantity,
                        wardrobeFormState.clotheList[i].quantity
                    )
                    assertNull(
                        "Quantity error is not have the default value",
                        wardrobeFormState.clotheList[i].quantityError
                    )
                    assertEquals(
                        "The type is not correct",
                        testUserWardrobeWithClothes.listClothe[i].typeId,
                        wardrobeFormState.clotheList[i].type
                    )
                }
            } else {
                fail("Clothe list does not have the correct size, Expected:<${testUserWardrobeWithClothes.listClothe.size}>, but was<${wardrobeFormState.clotheList.size}>")
            }
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
        assertEquals("The list is not the correct size", 1, fakeRepository.listUserWardrobeWithClothes.size)
    }
}