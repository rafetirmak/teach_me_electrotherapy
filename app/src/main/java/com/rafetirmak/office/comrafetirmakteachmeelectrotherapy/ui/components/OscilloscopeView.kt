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
import androidx.compose.ui.draw.clipToBounds
import kotlinx.coroutines.delay

data class WaveformChannel(
    val name: String,
    val color: Color,
    val centerYPercent: Float = 0.5f, // 0.0 to 1.0 of height
    val scaleY: Float = 2.5f,
    val expectedMaxFreq: Float = 100f, // Added for dynamic sampling
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
        var lastTime = 0L
        while (true) {
            withFrameMillis { frameTime ->
                if (lastTime != 0L) {
                    val deltaSec = (frameTime - lastTime) / 1000.0
                    zamanOffset += deltaSec * 0.02 
                }
                lastTime = frameTime
            }
        }
    }

    Box(modifier = modifier
        .background(Color.White)
        .clipToBounds() // Prevents drawing outside the oscilloscope area
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // 1. Grid Draw
            val stepX = width / 10
            for (i in 0..10) {
                val x = i * stepX
                drawLine(Color(0xFFECF0F1), Offset(x, 0f), Offset(x, height), strokeWidth = 1f)
            }
            val stepY = height / 10
            for (i in 0..10) {
                val y = i * stepY
                drawLine(Color(0xFFECF0F1), Offset(0f, y), Offset(width, y), strokeWidth = 1f)
            }

            channels.forEach { channel ->
                val centerY = height * channel.centerYPercent
                drawLine(Color(0xFFBDC3C7), Offset(0f, centerY), Offset(width, centerY), strokeWidth = 1.5f)

                // --- DYNAMIC SAMPLING (Anti-Aliasing Logic) ---
                val totalTimeOnScreen = (timebaseMs / 1000.0) * 10.0
                
                // Nyquist Rule: Sample at least 2x freq. 
                // For visual beauty, we use 10x to 20x.
                val visualOversamplingFactor = 15f 
                val requiredSamplingRate = channel.expectedMaxFreq * visualOversamplingFactor
                
                // Calculate total samples needed for the current screen duration
                val targetSamples = (requiredSamplingRate * totalTimeOnScreen).toInt()
                
                // Clamp samples between [width] and [width * 10] to maintain performance
                val totalSamples = targetSamples.coerceIn(width.toInt(), width.toInt() * 10)
                val timePerSample = totalTimeOnScreen / totalSamples

                val path = Path()
                for (s in 0 until totalSamples) {
                    val x = (s.toFloat() / totalSamples) * width
                    val t = (s * timePerSample) + zamanOffset
                    val valY = channel.onDrawWaveform(t)
                    val y = centerY - (valY * channel.scaleY)

                    if (s == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }

                drawPath(path = path, color = channel.color, style = Stroke(width = 2.5f))
            }
        }
    }
}
