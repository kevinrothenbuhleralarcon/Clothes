package ch.kra.wardrobe.cloth_list.presentation.list_wardrobe.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ch.kra.wardrobe.R
import ch.kra.wardrobe.cloth_list.presentation.list_wardrobe.ListWardrobeEvents
import ch.kra.wardrobe.cloth_list.presentation.list_wardrobe.ListWardrobeViewModel
import ch.kra.wardrobe.core.UIEvent
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ListWardrobeScreen(
    viewModel: ListWardrobeViewModel = hiltViewModel(),
    navigate: (UIEvent.Navigate) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val wardrobes = viewModel.wardrobes.collectAsState(initial = emptyList()).value

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.Navigate -> navigate(event)
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.wardrobes))
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onEvent(ListWardrobeEvents.NewWardrobe) }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_wardrobe)
                )
            }
        }
    ) { pv ->
        LazyColumn(
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier
                .padding(pv)
                .fillMaxSize()
        ) {
            items(wardrobes.size) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.onEvent(
                                ListWardrobeEvents.EditWardrobe(
                                    wardrobes[it].id ?: 0
                                )
                            )
                        }
                        .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(5.dp))
                ) {
                    Text(text = wardrobes[it].username)
                    Text(text = wardrobes[it].location)
                    Text(text = SimpleDateFormat.getDateTimeInstance().format(wardrobes[it].lastUpdated))
                }
            }
        }
    }
}