package ch.kra.wardrobe.cloth_list.presentation.add_edit_wardrobe

sealed class AlertDialogSelection {
    object PositiveSelection: AlertDialogSelection()
    object NegativeSelection: AlertDialogSelection()
}
