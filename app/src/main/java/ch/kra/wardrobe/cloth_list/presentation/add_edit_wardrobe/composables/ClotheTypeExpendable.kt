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
import ch.kra.wardrobe.cloth_list.domain.model.Clothe
import ch.kra.wardrobe.cloth_list.presentation.add_edit_wardrobe.ClotheFormState
import ch.kra.wardrobe.core.ClotheType

@Composable
fun ClotheTypeExpendable(
    isExpended: Boolean,
    type: String,
    contentList: List<Clothe>,
    modifier: Modifier = Modifier,
    onTypeClick: () -> Unit,
    onContentClick: (Int) -> Unit
) {
    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onTypeClick() }
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
                    text = contentList.size.toString(),
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
                contentList.forEachIndexed { index, clothe ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onContentClick(index) }
                            .padding(
                                vertical = 8.dp,
                                horizontal = 32.dp
                            )
                    ) {
                        Text(
                            text = clothe.clothe,
                            fontSize = 16.sp
                        )
                        Text(
                            text = clothe.quantity.toString(),
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