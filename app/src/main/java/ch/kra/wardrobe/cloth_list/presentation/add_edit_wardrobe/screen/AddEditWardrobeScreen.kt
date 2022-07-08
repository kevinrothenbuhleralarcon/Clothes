package ch.kra.wardrobe.cloth_list.presentation.add_edit_wardrobe.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import ch.kra.wardrobe.R
import ch.kra.wardrobe.cloth_list.presentation.add_edit_wardrobe.AddEditWardrobeEvents
import ch.kra.wardrobe.cloth_list.presentation.add_edit_wardrobe.AddEditWardrobeViewModel
import ch.kra.wardrobe.core.AlertDialogSelection
import ch.kra.wardrobe.cloth_list.presentation.add_edit_wardrobe.ClotheFormState
import ch.kra.wardrobe.core.ClotheType
import ch.kra.wardrobe.core.UIEvent
import ch.kra.wardrobe.core.shared_composable.ValidationDialog

@ExperimentalComposeUiApi
@Composable
fun AddEditWardrobeScreen(
    viewModel: AddEditWardrobeViewModel = hiltViewModel(),
    popBackStack: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val wardrobeFormState = viewModel.wardrobeFormState.value
    val currentClotheState = viewModel.currentClothe.value
    val displayClotheForm = viewModel.displayClotheForm.value
    val displayBackDialog = viewModel.displayBackDialog.value
    val displayDeleteDialog = viewModel.displayDeleteDialog.value

    BackHandler {
        viewModel.onEvent(AddEditWardrobeEvents.NavigateBackPressed())
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.PopBackStack -> {
                    popBackStack()
                }

                else -> {}
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    if (wardrobeFormState.id == null)
                        Text(text = stringResource(R.string.new_wardrobe))
                    else
                        Text(text = stringResource(R.string.edit_wardrobe))
                },
                navigationIcon = wardrobeFormState.id?.let {
                    @Composable {
                        IconButton(onClick = { viewModel.onEvent(AddEditWardrobeEvents.DeleteWardrobe()) }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.delete)
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.onEvent(AddEditWardrobeEvents.AddClothe) }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.add_clothe)
                        )
                    }

                    IconButton(onClick = { viewModel.onEvent(AddEditWardrobeEvents.SaveWardrobe) }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(id = R.string.save)
                        )
                    }
                }
            )
        }
    ) { pv ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(pv)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f),
                contentPadding = PaddingValues(8.dp)
            ) {
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        ClotheDialog(
                            showDialog = displayClotheForm,
                            data = currentClotheState,
                            onEvent = { viewModel.onEvent(it) })

                        OutlinedTextField(
                            value = wardrobeFormState.username,
                            onValueChange = {
                                viewModel.onEvent(
                                    AddEditWardrobeEvents.UsernameChanged(
                                        it
                                    )
                                )
                            },
                            label = { Text(text = stringResource(R.string.username)) },
                            isError = wardrobeFormState.usernameError != null,
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (wardrobeFormState.usernameError != null) {
                            Text(
                                text = wardrobeFormState.usernameError.asString(),
                                color = MaterialTheme.colors.error
                            )
                        }

                        OutlinedTextField(
                            value = wardrobeFormState.location,
                            onValueChange = {
                                viewModel.onEvent(
                                    AddEditWardrobeEvents.LocationChanged(
                                        it
                                    )
                                )
                            },
                            label = { Text(text = stringResource(R.string.location)) },
                            isError = wardrobeFormState.locationError != null,
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (wardrobeFormState.locationError != null) {
                            Text(
                                text = wardrobeFormState.locationError.asString(),
                                color = MaterialTheme.colors.error
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                        )
                    }
                }

                items(wardrobeFormState.clotheList.size) { id ->
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.onEvent(AddEditWardrobeEvents.UpdateClothe(id)) }) {
                        Text(text = wardrobeFormState.clotheList[id].clothe)
                        Text(text = wardrobeFormState.clotheList[id].quantity.toString())
                    }
                }
            }
        }
    }

    // Navigate back alert dialog
    ValidationDialog(
        display = displayBackDialog,
        title = stringResource(R.string.back_alert_title),
        text = stringResource(R.string.back_alert_dialog_message),
        onPositiveSelection = {
            viewModel.onEvent(
                AddEditWardrobeEvents.NavigateBackPressed(
                    AlertDialogSelection.PositiveSelection
                )
            )
        },
        onNegativeSelection = {
            viewModel.onEvent(
                AddEditWardrobeEvents.NavigateBackPressed(
                    AlertDialogSelection.NegativeSelection
                )
            )
        }
    )

    // Delete wardrobe alert dialog
    ValidationDialog(
        display = displayDeleteDialog,
        title = stringResource(id = R.string.delete),
        text = stringResource(id = R.string.delete_alert_dialog_message),
        onPositiveSelection = {
            viewModel.onEvent(
                AddEditWardrobeEvents.DeleteWardrobe(
                    AlertDialogSelection.PositiveSelection
                )
            )
        },
        onNegativeSelection = {
            viewModel.onEvent(
                AddEditWardrobeEvents.DeleteWardrobe(
                    AlertDialogSelection.NegativeSelection
                )
            )
        }
    )
}

@ExperimentalComposeUiApi
@Composable
private fun ClotheDialog(
    showDialog: Boolean,
    data: ClotheFormState,
    onEvent: (AddEditWardrobeEvents) -> Unit
) {
    val clotheType = mutableListOf<String>()
    enumValues<ClotheType>().forEach {
        clotheType.add(stringResource(id = it.resId))
    }
    if (showDialog) {
        Dialog(
            onDismissRequest = { onEvent(AddEditWardrobeEvents.CancelClothe) },
            properties = DialogProperties(usePlatformDefaultWidth = false) // Workaround so that the dialog resize correctly
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colors.surface,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = data.clothe,
                        onValueChange = { onEvent(AddEditWardrobeEvents.ClotheChanged(it)) },
                        label = { Text(text = stringResource(R.string.clothe)) },
                        isError = data.clotheError != null,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (data.clotheError != null) {
                        Text(
                            text = data.clotheError.asString(),
                            color = MaterialTheme.colors.error
                        )
                    }

                    OutlinedTextField(
                        value = data.quantity?.toString() ?: "",
                        onValueChange = {
                            onEvent(
                                AddEditWardrobeEvents.QuantityChanged(
                                    it.toIntOrNull()
                                )
                            )
                        },
                        label = { Text(text = stringResource(R.string.quantity)) },
                        isError = data.quantityError != null,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (data.quantityError != null) {
                        Text(
                            text = data.quantityError.asString(),
                            color = MaterialTheme.colors.error
                        )
                    }

                    ClotheTypeDropDown(
                        type = data.type,
                        onItemSelected = { onEvent(AddEditWardrobeEvents.TypeChanged(it)) }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Button(
                            modifier = Modifier.fillMaxWidth(if (!data.update) 1f else 0.5f),
                            onClick = { onEvent(AddEditWardrobeEvents.SaveClothe) }
                        ) {
                            Text(text = stringResource(R.string.save))
                        }

                        if (data.update) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Button(
                                onClick = { onEvent(AddEditWardrobeEvents.DeleteClothe) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = stringResource(R.string.delete))
                            }
                        }
                    }
                }
            }
        }
    }
}