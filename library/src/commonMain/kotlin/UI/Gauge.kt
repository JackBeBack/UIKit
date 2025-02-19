package io.github.kotlin.fibonacci.UI

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.drawscope.*
import kotlin.math.*

@Composable
fun GaugeDisplay(
    value: Float,
    unit: String = "%",
    modifier: Modifier = Modifier,
    minValue: Float = 0f,
    maxValue: Float = 100f
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Gauge(
            value = value,
            minValue = minValue,
            maxValue = maxValue,
            modifier = Modifier.fillMaxSize()
        )
        Text(
            text = "${String.format("%.1f", value)} $unit",
            style = MaterialTheme.typography.h4,
            color = Color.Black
        )
    }
}

@Composable
fun Gauge(
    value: Float,
    minValue: Float,
    maxValue: Float,
    modifier: Modifier = Modifier
) {
    // Animate the incoming value
    val animatedValue by animateFloatAsState(
        targetValue = value.coerceIn(minValue, maxValue),
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
    )

    // Angles for the 3/4 circle
    val startAngle = 135f
    val sweepAngle = 270f

    Canvas(modifier = modifier) {
        val center = center
        val strokeWidth = size.minDimension * 0.1f
        val radius = (size.minDimension - strokeWidth) / 2f

        // Draw background arc
        drawArc(
            color = Color.LightGray,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // Calculate how much of the arc to fill
        val progressAngle = (animatedValue - minValue) / (maxValue - minValue) * sweepAngle

        // Draw progress arc
        drawArc(
            color = Color(0xFF4CAF50),
            startAngle = startAngle,
            sweepAngle = progressAngle,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // Draw needle
        val needleAngle = (startAngle + progressAngle) * (Math.PI / 180f)
        val needleEnd = Offset(
            x = center.x + radius * cos(needleAngle).toFloat(),
            y = center.y + radius * sin(needleAngle).toFloat()
        )
        val needleDir = center.minus(needleEnd)
        val needleStart = center.minus(needleDir.div(needleDir.getDistance()).times(80f))
        drawLine(
            color = Color.Black,
            start = needleStart,
            end = needleEnd,
            strokeWidth = strokeWidth * 0.4f,
            cap = StrokeCap.Round
        )
    }
}

