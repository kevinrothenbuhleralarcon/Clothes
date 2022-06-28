package ch.kra.wardrobe.cloth_list.presentation.list_wardrobe

import app.cash.turbine.test
import ch.kra.wardrobe.MainDispatcherRule
import ch.kra.wardrobe.cloth_list.core.TestDispatcher
import ch.kra.wardrobe.cloth_list.data.repository.FakeWardrobeRepositoryImpl
import ch.kra.wardrobe.cloth_list.domain.model.UserWardrobe
import ch.kra.wardrobe.cloth_list.domain.model.UserWardrobeWithClothes
import ch.kra.wardrobe.cloth_list.domain.repository.WardrobeRepository
import ch.kra.wardrobe.cloth_list.domain.use_case.GetWardrobes
import ch.kra.wardrobe.core.Constants.NAVIGATION_WARDROBE_ID
import ch.kra.wardrobe.core.Routes
import ch.kra.wardrobe.core.UIEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class ListWardrobeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var testDispatcher: TestDispatcher
    private lateinit var fakeRepository: FakeWardrobeRepositoryImpl
    private lateinit var viewModel: ListWardrobeViewModel

    @Before
    fun setUp() {
        testDispatcher = TestDispatcher()
        fakeRepository = FakeWardrobeRepositoryImpl()
        viewModel = ListWardrobeViewModel(
            GetWardrobes(fakeRepository),
            testDispatcher
        )
    }

    @Test
    fun `test get wardrobes`() = runTest {
        viewModel.wardrobes.test {
            val wardrobes = awaitItem()
            assertEquals("Wardrobe list is not empty", 0, wardrobes.size)
            cancelAndIgnoreRemainingEvents()
        }

        fakeRepository.listUserWardrobeWithClothes.add(
            UserWardrobeWithClothes(
                userWardrobe = UserWardrobe(
                    username = "test",
                    location = "testLocation",
                    lastUpdated = Date()
                ),
                listClothe = emptyList()
            )
        )

        viewModel.wardrobes.test {
            val wardrobes = awaitItem()
            assertEquals("Wardrobe list is empty", 1, wardrobes.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent with NewWardrobe, event is sent to navigate`() = runTest {
        viewModel.onEvent(ListWardrobeEvents.NewWardrobe)
        viewModel.uiEvent.test {
            val event = awaitItem()
            if (event is UIEvent.Navigate)
                assertEquals("The route is not correct", Routes.ADD_EDIT_WARDROBE, event.route)
            else
                fail("event is not UIEvent.Navigate")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent with EditWardrobe, event is sent to navigate with the correct id`() = runTest {
        val id = 1
        viewModel.onEvent(ListWardrobeEvents.EditWardrobe(id = id))
        viewModel.uiEvent.test {
            val event = awaitItem()
            if (event is UIEvent.Navigate)
                assertEquals("The route is not correct", Routes.ADD_EDIT_WARDROBE + "?$NAVIGATION_WARDROBE_ID=$id", event.route)
            else
                fail("event is not UIEvent.Navigate")
            cancelAndIgnoreRemainingEvents()
        }
    }
}