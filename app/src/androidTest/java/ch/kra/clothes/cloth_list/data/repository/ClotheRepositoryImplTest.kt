package ch.kra.clothes.cloth_list.data.repository

import ch.kra.clothes.cloth_list.di.ClotheListModule
import ch.kra.clothes.cloth_list.domain.model.UserList
import ch.kra.clothes.cloth_list.domain.model.UserListWithClothes
import ch.kra.clothes.cloth_list.domain.repository.ClotheRepository
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
@UninstallModules(ClotheListModule::class)
class ClotheRepositoryImplTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var clotheRepository: ClotheRepository

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun testInsertUserListAndRetrieveIt() = runBlocking {
        val newUserList = UserList(
            username = "test",
            location = "Lausanne",
            lastUpdated = Date()
        )
        val userListWithClothes = UserListWithClothes(
            userList = newUserList,
            listClothe = emptyList()
        )
        clotheRepository.addUserListWithClothes(userListWithClothes)

        val flow = clotheRepository.getUsersList()
        val userList = flow.first()
        assertEquals("The list should contain 1 item", 1, userList.size)
        assertEquals("The username is not correct", "test", userList[0].username)
    }
}