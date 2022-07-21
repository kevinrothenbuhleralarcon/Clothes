package ch.kra.wardrobe.cloth_list.presentation.add_edit_wardrobe

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.kra.wardrobe.cloth_list.domain.model.Clothe
import ch.kra.wardrobe.cloth_list.domain.model.UserWardrobe
import ch.kra.wardrobe.cloth_list.domain.model.UserWardrobeWithClothes
import ch.kra.wardrobe.cloth_list.domain.use_case.*
import ch.kra.wardrobe.core.*
import ch.kra.wardrobe.core.Constants.NAVIGATION_WARDROBE_ID
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

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var hasUnsavedChanged = false

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
                hasUnsavedChanged = true
            }

            is AddEditWardrobeEvents.LocationChanged -> {
                _wardrobeFormState.value = wardrobeFormState.value.copy(
                    location = event.location
                )
                hasUnsavedChanged = true
            }

            is AddEditWardrobeEvents.ClotheChanged -> {
                _wardrobeFormState.value = wardrobeFormState.value.copy(
                    currentClothe = wardrobeFormState.value.currentClothe.copy(
                        clothe = event.clothe
                    )
                )
            }

            is AddEditWardrobeEvents.QuantityChanged -> {
                _wardrobeFormState.value = wardrobeFormState.value.copy(
                    currentClothe = wardrobeFormState.value.currentClothe.copy(
                        quantity = event.quantity
                    )
                )
            }

            is AddEditWardrobeEvents.TypeChanged -> {
                _wardrobeFormState.value = wardrobeFormState.value.copy(
                    currentClothe = wardrobeFormState.value.currentClothe.copy(
                        type = event.clotheType
                    )
                )
            }

            is AddEditWardrobeEvents.AddClothe -> {
                _wardrobeFormState.value = wardrobeFormState.value.copy(
                    currentClothe = ClotheFormState(
                        displayClotheForm = true
                    )
                )
            }

            is AddEditWardrobeEvents.UpdateClothe -> {
                val clothe =
                    wardrobeFormState.value.clothesByType[event.type]?.clotheList?.get(event.id)
                clothe?.let {
                    _wardrobeFormState.value = wardrobeFormState.value.copy(
                        currentClothe = ClotheFormState(
                            id = it.id,
                            clothe = it.clothe,
                            quantity = it.quantity,
                            type = it.type,
                            originalType = it.type,
                            originalListIndex = event.id,
                            displayClotheForm = true
                        )
                    )
                }
            }

            is AddEditWardrobeEvents.SaveClothe -> {
                _wardrobeFormState.value = wardrobeFormState.value.copy(
                    currentClothe = wardrobeFormState.value.currentClothe.copy(
                        clotheError = null,
                        quantityError = null
                    )
                )

                val clotheResult = validateClothe(wardrobeFormState.value.currentClothe.clothe)
                val quantityResult =
                    validateQuantity(wardrobeFormState.value.currentClothe.quantity)

                val hasError = listOf(
                    clotheResult,
                    quantityResult
                ).any { !it.successful }

                if (hasError) {
                    _wardrobeFormState.value = wardrobeFormState.value.copy(
                        currentClothe = wardrobeFormState.value.currentClothe.copy(
                            clotheError = clotheResult.errorMessage,
                            quantityError = quantityResult.errorMessage
                        )
                    )
                    return
                }

                if (wardrobeFormState.value.currentClothe.originalType == null) {
                    addClothe()
                } else {
                    updateClothe()
                }
                hasUnsavedChanged = true
            }

            is AddEditWardrobeEvents.CancelClothe -> {
                _wardrobeFormState.value = wardrobeFormState.value.copy(
                    currentClothe = ClotheFormState()
                )
            }

            is AddEditWardrobeEvents.DeleteClothe -> {
                removeCurrentClothe()
                _wardrobeFormState.value = wardrobeFormState.value.copy(
                    currentClothe = ClotheFormState()
                )
                hasUnsavedChanged = true
            }

            is AddEditWardrobeEvents.SaveWardrobe -> {
                submitData()
            }

            is AddEditWardrobeEvents.DeleteWardrobe -> {
                _wardrobeFormState.value = wardrobeFormState.value.copy(
                    displayDeleteDialog = false
                )
                when (event.dialogSelection) {
                    is AlertDialogSelection.PositiveSelection -> {
                        deleteWardrobe()
                    }

                    is AlertDialogSelection.NegativeSelection -> {}

                    else -> {
                        _wardrobeFormState.value = wardrobeFormState.value.copy(
                            displayDeleteDialog = true
                        )
                    }
                }
            }

            is AddEditWardrobeEvents.NavigateBackPressed -> {
                _wardrobeFormState.value = wardrobeFormState.value.copy(
                    displayBackDialog = false
                )
                when (event.dialogSelection) {
                    is AlertDialogSelection.PositiveSelection -> {
                        sendUiEvent(UIEvent.PopBackStack)
                    }

                    is AlertDialogSelection.NegativeSelection -> { /*Nothing*/
                    }

                    else -> {
                        if (hasUnsavedChanged) {
                            _wardrobeFormState.value = wardrobeFormState.value.copy(
                                displayBackDialog = true
                            )
                        } else {
                            sendUiEvent(UIEvent.PopBackStack)
                        }
                    }
                }
            }

            is AddEditWardrobeEvents.TypeClicked -> {
                _wardrobeFormState.value = wardrobeFormState.value.copy(
                    clothesByType = wardrobeFormState.value.clothesByType.entries.associate {
                        if (it.key == event.type) {
                            it.key to it.value.copy(isExpanded = !it.value.isExpanded)
                        } else {
                            it.key to it.value
                        }
                    }
                )
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
                    clothesByType = wardrobe.listClothe
                        .groupBy { it.type }
                        .mapValues { ClotheListState(clotheList = it.value) }
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
                    listClothe = wardrobeFormState.value.clothesByType.flatMap {
                        it.value.clotheList
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
                    listClothe = wardrobeFormState.value.clothesByType.flatMap {
                        it.value.clotheList
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

    private fun addClothe() {
        wardrobeFormState.value.apply {
            if (this.currentClothe.type in this.clothesByType) {
                _wardrobeFormState.value = this.copy(
                    clothesByType = this.clothesByType.entries.associate {
                        if (it.key == this.currentClothe.type) {
                            it.key to it.value.copy(
                                clotheList = it.value.clotheList + Clothe(
                                    clothe = this.currentClothe.clothe,
                                    quantity = this.currentClothe.quantity ?: 0,
                                    type = this.currentClothe.type
                                )
                            )
                        } else {
                            it.key to it.value
                        }
                    },
                    currentClothe = ClotheFormState()
                )
            } else {
                _wardrobeFormState.value = this.copy(
                    clothesByType = this.clothesByType + (this.currentClothe.type to ClotheListState(
                        clotheList = listOf(
                            Clothe(
                                id = this.currentClothe.id,
                                clothe = this.currentClothe.clothe,
                                quantity = this.currentClothe.quantity ?: 0,
                                type = this.currentClothe.type
                            )
                        )
                    )),
                    currentClothe = ClotheFormState()
                )
            }
        }
    }

    private fun updateClothe() {
        wardrobeFormState.value.apply {
            if (this.currentClothe.type == this.currentClothe.originalType) {
                _wardrobeFormState.value = this.copy(
                    clothesByType = clothesByType.entries.associate {
                        if (it.key == this.currentClothe.type) {
                            it.key to it.value.copy(
                                clotheList = it.value.clotheList.mapIndexed { index, clothe ->
                                    if (index == this.currentClothe.originalListIndex) {
                                        Clothe(
                                            id = this.currentClothe.id,
                                            clothe = this.currentClothe.clothe,
                                            quantity = this.currentClothe.quantity ?: 0,
                                            type = this.currentClothe.type
                                        )
                                    } else {
                                        clothe
                                    }
                                }
                            )
                        } else {
                            it.key to it.value
                        }
                    },
                    currentClothe = ClotheFormState()
                )
            } else {
                removeCurrentClothe()
                addClothe()
            }
        }
    }

    private fun removeCurrentClothe() {
        wardrobeFormState.value.apply {
            this.currentClothe.originalType?.let { type ->
                if (this.clothesByType[type]?.clotheList?.size == 1) {
                    _wardrobeFormState.value = this.copy(
                        clothesByType = this.clothesByType.minus(type)
                    )
                } else {
                    this.currentClothe.originalListIndex?.let { index ->
                        _wardrobeFormState.value = this.copy(
                            clothesByType = this.clothesByType.entries.associate {
                                if (it.key == this.currentClothe.originalType) {
                                    it.key to it.value.copy(
                                        clotheList = it.value.clotheList.minus(it.value.clotheList[index])
                                    )
                                } else {
                                    it.key to it.value
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}