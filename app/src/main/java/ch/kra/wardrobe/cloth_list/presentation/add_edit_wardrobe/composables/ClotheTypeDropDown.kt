package ch.kra.wardrobe.cloth_list.presentation.add_edit_wardrobe.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import ch.kra.wardrobe.R
import ch.kra.wardrobe.core.ClotheType

@Composable
fun ClotheTypeDropDown(
    type: ClotheType,
    modifier: Modifier = Modifier,
    onItemSelected: (ClotheType) -> Unit,
) {
    var expend by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = stringResource(id = type.resId),
            onValueChange = {},
            singleLine = true,
            label = { Text(text = stringResource(id = R.string.clothe_type)) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned {
                    textFieldSize = it.size.toSize()
                },
            trailingIcon = {
                Box(modifier = Modifier.clickable { expend = !expend }) {
                    if (expend) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = stringResource(id = R.string.clothe_type),
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = stringResource(id = R.string.clothe_type),
                        )
                    }
                }
            }
        )
        DropdownMenu(
            expanded = expend,
            onDismissRequest = { expend = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() - 35.dp })
        ) {
            enumValues<ClotheType>().forEach { clotheType ->
                DropdownMenuItem(
                    onClick = {
                        onItemSelected(clotheType)
                        expend = false
                    }
                ) {
                    Text(text = stringResource(id = clotheType.resId))
                }
            }
        }
    }
}