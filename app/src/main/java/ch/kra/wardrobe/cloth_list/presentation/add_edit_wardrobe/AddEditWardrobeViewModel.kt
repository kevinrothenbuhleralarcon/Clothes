package ch.kra.wardrobe.cloth_list.presentation.add_edit_wardrobe

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.kra.wardrobe.cloth_list.domain.use_case.*
import ch.kra.wardrobe.core.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
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
        savedStateHandle.get<Int>("wardrobeId")?.let { id ->
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

                _displayClotheForm.value = false
            }

            is AddEditWardrobeEvents.DeleteClothe -> {

            }

            is AddEditWardrobeEvents.SaveWardrobe -> {

            }

            is AddEditWardrobeEvents.DeleteWardrobe -> {

            }
        }
    }

    private fun sendUiEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    private fun getWardrobe(id: Int) {
        viewModelScope.launch {
            getWardrobeWithClothesById(id).onEach { wardrobe ->
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
                    }.sortedWith(compareBy<ClotheFormState> { it.id }.thenBy { it.clothe })
                )
            }.launchIn(this)
        }
    }
}