package ch.kra.wardrobe.core

sealed class AlertDialogSelection {
    object PositiveSelection: AlertDialogSelection()
    object NegativeSelection: AlertDialogSelection()
}
