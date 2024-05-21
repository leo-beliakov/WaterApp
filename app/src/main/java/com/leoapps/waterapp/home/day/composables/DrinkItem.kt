package com.leoapps.waterapp.home.day.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoapps.waterapp.common.presentation.theme.WaterAppTheme
import com.leoapps.waterapp.home.day.model.DrinkItem

@Composable
fun DrinkItem(
    drinkItem: DrinkItem,
    onClick: (DrinkItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding()
            .clip(RoundedCornerShape(12.dp))
            .background(drinkItem.color)
            .clickable { onClick(drinkItem) }
            .padding(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(64.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            Image(
                painter = painterResource(drinkItem.iconResId),
                contentDescription = null
            )
        }
        Text(
            text = stringResource(drinkItem.titleResId),
            textAlign = TextAlign.Center,
            style = TextStyle.Default.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight(700)
            ),
            modifier = Modifier.weight(
                weight = 1f,
                fill = true
            )
        )
    }
}

@Preview
@Composable
private fun DrinkItemPreview() {
    WaterAppTheme {
        DrinkItem(
            drinkItem = DrinkItem.COFFEE,
            onClick = {},
            modifier = Modifier.width(200.dp)
        )
    }
}