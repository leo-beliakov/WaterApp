package com.leoapps.waterapp.home.day.composables.progress

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoapps.waterapp.ui.theme.Gray
import com.leoapps.waterapp.ui.theme.PurpleMain
import com.leoapps.waterapp.ui.theme.PurpleOpaque
import com.leoapps.waterapp.ui.theme.White
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CircleProgress(
    title: String,
    subtitle: String,
    progressState: ProgressState = rememberProgressState(),
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val measuredTitle = remember(title) {
        textMeasurer.measure(
            text = title,
            style = TextStyle(
                fontSize = 40.sp,
                fontWeight = FontWeight(700),
                color = Color.Black
            )
        )
    }
    val measuredSubtitle = remember(subtitle) {
        textMeasurer.measure(
            text = subtitle,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight(700),
                color = Color.Gray
            )
        )
    }

    Spacer(
        modifier = modifier
            .aspectRatio(1f)
            .drawWithCache {
                //Main circle calculations
                val strokeWidth = size.width * 0.1f
                val strokeWidthHalf = strokeWidth / 2
                val radius = (size.width - strokeWidth) / 2
                val topLeftOffset = Offset(strokeWidthHalf, strokeWidthHalf)
                val circleSize = Size(size.width - strokeWidth, size.height - strokeWidth)

                //Progress circle calculations
                val progressAngle = 360 * progressState.value

                //Decoration circle calculations
                val decorationCircleRadius = strokeWidthHalf * 0.6f
                val endAngleRad = Math.toRadians((progressAngle - 90).toDouble())

                val decorationCenter = Offset(
                    size.center.x + radius * cos(endAngleRad).toFloat(),
                    size.center.y + radius * sin(endAngleRad).toFloat()
                )

                //Decoration circle dots calculations
                val dotRadius = decorationCircleRadius * 0.2f
                val dotDistance = decorationCircleRadius * 0.35f
                val angleRads = List(4) { Math.toRadians((it * 90 + 45).toDouble()) }
                val dotsOffsets = angleRads.map {
                    Offset(
                        decorationCenter.x + dotDistance * cos(it).toFloat(),
                        decorationCenter.y + dotDistance * sin(it).toFloat()
                    )
                }

                //Text calculations
                val subtitleTopLeft =
                    size.center - Offset(+measuredSubtitle.size.width / 2f, -20.dp.toPx())
                val titleTopLeft =
                    size.center - Offset(measuredTitle.size.width / 2f, measuredTitle.firstBaseline)

                onDrawWithContent {
                    // Draw the background circle
                    drawCircle(
                        color = Gray,
                        radius = radius,
                        center = center,
                        style = Stroke(strokeWidth)
                    )

                    // Draw the progress circle
                    drawArc(
                        color = PurpleMain,
                        startAngle = -90f,
                        sweepAngle = progressAngle,
                        useCenter = false,
                        topLeft = topLeftOffset,
                        size = circleSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )

                    // Draw the decoration circle at the end of the progress
                    drawCircle(
                        color = White,
                        radius = decorationCircleRadius,
                        center = decorationCenter
                    )

                    //Draw the 4 decoration dots
                    repeat(4) { circleIndex ->
                        drawCircle(
                            color = PurpleOpaque,
                            radius = dotRadius,
                            center = dotsOffsets[circleIndex]
                        )
                    }

                    // Draw title
                    drawText(
                        textLayoutResult = measuredTitle,
                        topLeft = titleTopLeft
                    )

                    // Draw subtitle
                    drawText(
                        textLayoutResult = measuredSubtitle,
                        topLeft = subtitleTopLeft
                    )
                }
            }
    )
}


@Preview
@Composable
private fun CircleProgressPreview() {
    CircleProgress(
        title = "50%",
        subtitle = "1245 ml",
        progressState = rememberProgressState(0.25f),
        modifier = Modifier.size(300.dp),
    )
}