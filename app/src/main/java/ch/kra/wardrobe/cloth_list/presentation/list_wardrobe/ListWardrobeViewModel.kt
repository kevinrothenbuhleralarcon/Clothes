package ch.kra.wardrobe.cloth_list.presentation.list_wardrobe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.kra.wardrobe.cloth_list.domain.use_case.GetWardrobes
import ch.kra.wardrobe.core.DispatcherProvider
import ch.kra.wardrobe.core.Routes
import ch.kra.wardrobe.core.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListWardrobeViewModel @Inject constructor(
    private val getWardrobes: GetWardrobes,
    private val dispatcher: DispatcherProvider
): ViewModel() {

    val wardrobes = getWardrobes().flowOn(dispatcher.io)

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: ListWardrobeEvents) {
        when (event) {
            is ListWardrobeEvents.NewWardrobe -> {
                sendUIEvent(
                    UIEvent.Navigate(Routes.ADD_EDIT_WARDROBE)
                )
            }

            is ListWardrobeEvents.EditWardrobe -> {
                sendUIEvent(
                    UIEvent.Navigate(
                        Routes.ADD_EDIT_WARDROBE +
                                "?wardrobeId=${event.id}"
                    )
                )
            }
        }
    }

    private fun sendUIEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}