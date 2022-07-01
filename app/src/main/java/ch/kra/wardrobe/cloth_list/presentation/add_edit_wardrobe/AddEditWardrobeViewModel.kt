package ch.kra.wardrobe.cloth_list.presentation.add_edit_wardrobe

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.kra.wardrobe.cloth_list.domain.model.Clothe
import ch.kra.wardrobe.cloth_list.domain.model.UserWardrobe
import ch.kra.wardrobe.cloth_list.domain.model.UserWardrobeWithClothes
import ch.kra.wardrobe.cloth_list.domain.use_case.*
import ch.kra.wardrobe.core.Constants.NAVIGATION_WARDROBE_ID
import ch.kra.wardrobe.core.DispatcherProvider
import ch.kra.wardrobe.core.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddEditWardrobeViewModel @Inject constructor(
    private val getWardrobeWithClothesById: GetWardrobeWithClothesById,
    private val addWardrobeWithClothes: AddWardrobeWithClothes,
    private val updateWardrobeWithClothes: UpdateWardrobeWithClothes,
    private val deleteWardrobeWithClothes: DeleteWardrobeWithClothes,
    private val validateUsername: ValidateUsername,
    private val validateLocation: ValidateLocation,
    private val validateClothe: ValidateClothe,
    private val validateQuantity: ValidateQuantity,
    private val dispatcher: DispatcherProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _wardrobeFormState = mutableStateOf(WardrobeFormState())
    val wardrobeFormState: State<WardrobeFormState> = _wardrobeFormState

    private val _currentClothe = mutableStateOf(ClotheFormState())
    val currentClothe: State<ClotheFormState> = _currentClothe

    private val _displayClotheForm = mutableStateOf(false)
    val displayClotheForm: State<Boolean> = _displayClotheForm

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        savedStateHandle.get<Int>(NAVIGATION_WARDROBE_ID)?.let { id ->
            if (id > -1) getWardrobe(id)
        }
    }

    fun onEvent(event: AddEditWardrobeEvents) {
        when (event) {
            is AddEditWardrobeEvents.UsernameChanged -> {
                _wardrobeFormState.value = wardrobeFormState.value.copy(
                    username = event.username
                )
            }

            is AddEditWardrobeEvents.LocationChanged -> {
                _wardrobeFormState.value = wardrobeFormState.value.copy(
                    location = event.location
                )
            }

            is AddEditWardrobeEvents.ClotheChanged -> {
                _currentClothe.value = currentClothe.value.copy(
                    clothe = event.clothe
                )
            }

            is AddEditWardrobeEvents.QuantityChanged -> {
                _currentClothe.value = currentClothe.value.copy(
                    quantity = event.quantity
                )
            }

            is AddEditWardrobeEvents.TypeChanged -> {
                _currentClothe.value = currentClothe.value.copy(
                    type = event.type
                )
            }

            is AddEditWardrobeEvents.AddClothe -> {
                _currentClothe.value = ClotheFormState()
                _displayClotheForm.value = true
            }

            is AddEditWardrobeEvents.UpdateClothe -> {
                _currentClothe.value = wardrobeFormState.value.clotheList[event.id].copy()
                _displayClotheForm.value = true
            }

            is AddEditWardrobeEvents.SaveClothe -> {
                _currentClothe.value = currentClothe.value.copy(
                    clotheError = null,
                    quantityError = null
                )

                val clotheResult = validateClothe(currentClothe.value.clothe)
                val quantityResult = validateQuantity(currentClothe.value.quantity)

                val hasError = listOf(
                    clotheResult,
                    quantityResult
                ).any { !it.successful }

                if (hasError) {
                    _currentClothe.value = currentClothe.value.copy(
                        clotheError = clotheResult.errorMessage,
                        quantityError = quantityResult.errorMessage
                    )
                    return
                }

                if (currentClothe.value.id == null) {
                    _wardrobeFormState.value = wardrobeFormState.value.copy(
                        clotheList = wardrobeFormState.value.clotheList
                            .plus(currentClothe.value)
                            .sortedWith(compareBy<ClotheFormState> { it.type }.thenBy { it.clothe })
                    )
                } else {
                    _wardrobeFormState.value = wardrobeFormState.value.copy(
                        clotheList = wardrobeFormState.value.clotheList.map {
                            if (it.id == currentClothe.value.id)
                                currentClothe.value
                            else
                                it
                        }
                    )
                }
                _displayClotheForm.value = false
            }

            is AddEditWardrobeEvents.CancelClothe -> {
                _displayClotheForm.value = false
            }

            is AddEditWardrobeEvents.DeleteClothe -> {
                val elementToRemove =
                    wardrobeFormState.value.clotheList.find { it.id == currentClothe.value.id }
                elementToRemove?.let {
                    _wardrobeFormState.value = wardrobeFormState.value.copy(
                        clotheList = wardrobeFormState.value.clotheList.minus(it)
                    )
                }

                _displayClotheForm.value = false
            }

            is AddEditWardrobeEvents.SaveWardrobe -> {
                submitData()
            }

            is AddEditWardrobeEvents.DeleteWardrobe -> {
                deleteWardrobe()
            }

            is AddEditWardrobeEvents.NavigateBackPressed -> {
                Log.d("Test", "coucou")
                sendUiEvent(UIEvent.PopBackStack)
            }
        }
    }

    private fun sendUiEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    private fun getWardrobe(id: Int) {
        getWardrobeWithClothesById(id).onEach { wardrobeOrNull ->
            wardrobeOrNull?.let { wardrobe ->
                _wardrobeFormState.value = wardrobeFormState.value.copy(
                    id = wardrobe.userWardrobe.id,
                    username = wardrobe.userWardrobe.username,
                    location = wardrobe.userWardrobe.location,
                    clotheList = wardrobe.listClothe.map { clothe ->
                        ClotheFormState(
                            id = clothe.id,
                            clothe = clothe.clothe,
                            quantity = clothe.quantity,
                            type = clothe.typeId
                        )
                    }.sortedWith(compareBy<ClotheFormState> { it.type }.thenBy { it.clothe })
                )
            }

        }
            .flowOn(dispatcher.io)
            .launchIn(viewModelScope)
    }

    private fun submitData() {
        _wardrobeFormState.value = wardrobeFormState.value.copy(
            usernameError = null,
            locationError = null
        )
        val usernameResult = validateUsername(wardrobeFormState.value.username)
        val locationResult = validateLocation(wardrobeFormState.value.location)

        val hasError = listOf(
            usernameResult,
            locationResult
        ).any { !it.successful }

        if (hasError) {
            _wardrobeFormState.value = wardrobeFormState.value.copy(
                usernameError = usernameResult.errorMessage,
                locationError = locationResult.errorMessage
            )
            return
        }

        if (wardrobeFormState.value.id == null)
            addWardrobe()
        else
            updateWardrobe()

    }

    private fun addWardrobe() {
        viewModelScope.launch(dispatcher.io) {
            addWardrobeWithClothes(
                UserWardrobeWithClothes(
                    UserWardrobe(
                        id = null,
                        username = wardrobeFormState.value.username,
                        location = wardrobeFormState.value.location,
                        lastUpdated = Date()
                    ),
                    listClothe = wardrobeFormState.value.clotheList.map {
                        Clothe(
                            id = it.id,
                            clothe = it.clothe,
                            quantity = it.quantity,
                            typeId = it.type
                        )
                    }
                )
            )
            sendUiEvent(UIEvent.PopBackStack)
        }
    }

    private fun updateWardrobe() {
        viewModelScope.launch(dispatcher.io) {
            updateWardrobeWithClothes(
                UserWardrobeWithClothes(
                    UserWardrobe(
                        id = wardrobeFormState.value.id,
                        username = wardrobeFormState.value.username,
                        location = wardrobeFormState.value.location,
                        lastUpdated = Date()
                    ),
                    listClothe = wardrobeFormState.value.clotheList.map {
                        Clothe(
                            id = it.id,
                            clothe = it.clothe,
                            quantity = it.quantity,
                            typeId = it.type
                        )
                    }
                )
            )
            sendUiEvent(UIEvent.PopBackStack)
        }
    }

    private fun deleteWardrobe() {
        wardrobeFormState.value.id?.let {
            viewModelScope.launch(dispatcher.io) {
                deleteWardrobeWithClothes(it)
                sendUiEvent(UIEvent.PopBackStack)
            }
        }
    }
}