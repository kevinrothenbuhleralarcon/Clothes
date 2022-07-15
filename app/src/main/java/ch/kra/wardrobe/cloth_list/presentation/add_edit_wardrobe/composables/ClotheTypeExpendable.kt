package ch.kra.wardrobe.cloth_list.presentation.add_edit_wardrobe.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.kra.wardrobe.R
import ch.kra.wardrobe.cloth_list.presentation.add_edit_wardrobe.ClotheFormState
import ch.kra.wardrobe.core.ClotheType

@Composable
fun ClotheTypeExpendable(
    type: String,
    contentList: List<ClotheFormState>,
    startIndex: Int,
    endIndex: Int,
    modifier: Modifier = Modifier,
    onContentClick: (Int) -> Unit
) {
    var isExpended by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpended = !isExpended }
                .background(MaterialTheme.colors.primary)
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
        ) {
            Text(
                text = type,
                color = MaterialTheme.colors.onPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Row (horizontalArrangement = Arrangement.End) {
                Text(
                    text = ((endIndex + 1) - startIndex).toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onPrimary
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = if (isExpended)
                        Icons.Default.KeyboardArrowUp
                    else
                        Icons.Default.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.toggle_expansion),
                    tint = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        AnimatedVisibility(
            visible = isExpended
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                for (i in startIndex..endIndex) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onContentClick(i) }
                            .padding(
                                vertical = 8.dp,
                                horizontal = 32.dp
                            )
                    ) {
                        Text(
                            text = contentList[i].clothe,
                            fontSize = 16.sp
                        )
                        Text(
                            text = contentList[i].quantity.toString(),
                            fontSize = 16.sp,
                            modifier = Modifier.padding(end = 16.dp)
                        )

                    }
                    Divider(modifier = Modifier.height(1.dp))
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewClotheTypeExpendable() {
    val data = listOf(
        ClotheFormState(clothe = "T-shirt", quantity = 5, type = ClotheType.UPPER_BODY),
        ClotheFormState(clothe = "Shirt", quantity = 2, type = ClotheType.UPPER_BODY),
        ClotheFormState(clothe = "Coat", quantity = 1, type = ClotheType.UPPER_BODY),
        ClotheFormState(clothe = "Pullover", quantity = 1, type = ClotheType.UPPER_BODY),
        ClotheFormState(clothe = "Short", quantity = 8, type = ClotheType.LEG),
        ClotheFormState(clothe = "Jeans", quantity = 4, type = ClotheType.LEG),
        ClotheFormState(clothe = "Dress", quantity = 1, type = ClotheType.LEG),
        ClotheFormState(clothe = "Skirt", quantity = 3, type = ClotheType.LEG)
    )

    Column(modifier = Modifier.fillMaxSize()) {
        ClotheTypeExpendable(
            type = stringResource(id = ClotheType.UPPER_BODY.resId),
            contentList = data,
            startIndex = 0,
            endIndex = 3,
            modifier = Modifier.fillMaxWidth(),
            onContentClick = {}
        )

        ClotheTypeExpendable(
            type = stringResource(id = ClotheType.LEG.resId),
            contentList = data,
            startIndex = 4,
            endIndex = 7,
            modifier = Modifier.fillMaxWidth(),
            onContentClick = {}
        )
    }
}