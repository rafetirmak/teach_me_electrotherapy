package com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.delay

data class WaveformChannel(
    val name: String,
    val color: Color,
    val centerYPercent: Float = 0.5f, // 0.0 to 1.0 of height
    val scaleY: Float = 2.5f,
    val onDrawWaveform: (time: Double) -> Float
)

@Composable
fun OscilloscopeView(
    modifier: Modifier = Modifier,
    timebaseMs: Float = 2.0f,
    channels: List<WaveformChannel>
) {
    var zamanOffset by remember { mutableStateOf(0.0) }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { _ ->
                zamanOffset += 0.0005 
            }
            delay(16)
        }
    }

    Box(modifier = modifier.background(Color.White)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // 1. Grid Draw
            val stepX = width / 10
            for (i in 0..10) {
                val x = i * stepX
                drawLine(Color(0xFFECF0F1), Offset(x, 0f), Offset(x, height), strokeWidth = 1f)
            }
            val stepY = height / 10 // More grid lines for multi-channel
            for (i in 0..10) {
                val y = i * stepY
                drawLine(Color(0xFFECF0F1), Offset(0f, y), Offset(width, y), strokeWidth = 1f)
            }

            channels.forEach { channel ->
                val centerY = height * channel.centerYPercent
                
                // Channel Baseline
                drawLine(Color(0xFFBDC3C7), Offset(0f, centerY), Offset(width, centerY), strokeWidth = 1.5f)

                // 2. Waveform Draw
                val totalTimeOnScreen = (timebaseMs / 1000.0) * 10.0
                val timePerPixel = totalTimeOnScreen / width

                val path = Path()
                var lastY = centerY

                for (x in 0 until width.toInt()) {
                    val t = (x * timePerPixel) + zamanOffset
                    val valY = channel.onDrawWaveform(t)
                    val y = centerY - (valY * channel.scaleY)

                    if (x == 0) {
                        path.moveTo(x.toFloat(), y)
                    } else {
                        if (Math.abs(y - lastY) > 1f) {
                            path.lineTo(x.toFloat(), lastY)
                            path.lineTo(x.toFloat(), y)
                        } else {
                            path.lineTo(x.toFloat(), y)
                        }
                    }
                    lastY = y
                }

                drawPath(
                    path = path,
                    color = channel.color,
                    style = Stroke(width = 2.5f)
                )
            }
        }
    }
}
