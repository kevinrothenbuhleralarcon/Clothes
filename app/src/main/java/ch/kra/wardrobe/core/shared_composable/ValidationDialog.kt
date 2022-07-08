package ch.kra.wardrobe.core.shared_composable

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ch.kra.wardrobe.R

@Composable
fun ValidationDialog(
    display: Boolean,
    title: String,
    text: String,
    positiveButtonText: String = stringResource( id = R.string.yes),
    onPositiveSelection: () -> Unit,
    negativeButtonText: String = stringResource(id = R.string.no),
    onNegativeSelection: () -> Unit
) {
    if (display) {
        AlertDialog(
            onDismissRequest = { /*Nothing*/ },
            title = { Text(text = title) },
            text = { Text(text = text) },
            confirmButton = {
                Button(onClick = { onPositiveSelection() }) {
                    Text(text = positiveButtonText)
                }
            },
            dismissButton = {
                Button(onClick = { onNegativeSelection() }) {
                    Text(text = negativeButtonText)
                }
            }
        )
    }
}