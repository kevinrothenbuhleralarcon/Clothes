package ch.kra.wardrobe.core

sealed class UIEvent {
    data class Navigate(val route: String): UIEvent()
}
