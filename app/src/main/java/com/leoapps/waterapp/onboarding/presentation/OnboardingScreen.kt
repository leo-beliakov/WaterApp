package com.leoapps.waterapp.onboarding.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.leoapps.waterapp.R
import com.leoapps.waterapp.common.presentation.theme.PurpleLight
import com.leoapps.waterapp.common.presentation.theme.WaterAppTheme
import com.leoapps.waterapp.onboarding.presentation.composables.PulsatingButton

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onNextClicked: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(24.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_onboarding_girl),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
                .zIndex(1f)
                .graphicsLayer {
                    translationY = 45.dp.toPx()
                }
        )
        OnboardingCard(
            onNextClicked = onNextClicked,
            modifier = Modifier
                .fillMaxHeight(0.45f)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun OnboardingCard(
    onNextClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(
            space = 12.dp,
            alignment = Alignment.Vertical { size, space ->
                //Place the content of the card 70% closer to the bottom
                (space - size) * 7 / 10
            }
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(
                color = PurpleLight,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(32.dp)


    ) {
        Text(
            text = stringResource(id = R.string.onboarding_title),
            style = TextStyle.Default.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight(700),
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        )
        Text(
            text = stringResource(id = R.string.onboarding_subtitle),
            style = TextStyle.Default.copy(
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        )
        PulsatingButton(
            onClick = onNextClicked,
            modifier = Modifier.padding(top = 18.dp)
        )
    }
}

@Preview
@Composable
private fun OnboardingScreenPreview() {
    WaterAppTheme {
        OnboardingScreen(
            onNextClicked = {},
        )
    }
}

@Preview
@Composable
private fun OnboardingCardPreview() {
    WaterAppTheme {
        OnboardingCard(
            onNextClicked = {},
            modifier = Modifier.size(
                width = 200.dp,
                height = 300.dp
            )
        )
    }
}