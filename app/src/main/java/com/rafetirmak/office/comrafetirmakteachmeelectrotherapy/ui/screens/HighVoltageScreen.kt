package com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.ui.components.ControlSlider
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.ui.components.OscilloscopeView
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.ui.components.WaveformChannel
import java.util.Locale

import androidx.compose.ui.res.stringResource
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HighVoltageScreen(onBack: () -> Unit) {
    var amp by remember { mutableFloatStateOf(365f) }
    var freq by remember { mutableFloatStateOf(50f) }
    var timebase by remember { mutableFloatStateOf(5.0f) }
    var polarity by remember { mutableIntStateOf(1) } // 1 for +, -1 for -

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.current_high_voltage)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.label_back))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OscilloscopeView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                timebaseMs = timebase,
                channels = listOf(
                    WaveformChannel(
                        name = "HVPS",
                        color = if (polarity == 1) Color(0xFFE74C3C) else Color(0xFF3498DB),
                        scaleY = 0.4f, // Adjusted scale for high voltage values (0-500V)
                        expectedMaxFreq = 10000f, // High frequency components due to narrow pulses
                        onDrawWaveform = { t ->
                            calculateHVPSValue(t, amp, freq, polarity)
                        }
                    )
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Controls
            Column(modifier = Modifier.fillMaxWidth()) {
                ControlSlider(label = "${stringResource(R.string.label_voltage)}: ${amp.toInt()} V", value = amp, range = 0f..500f) { amp = it }
                ControlSlider(label = "${stringResource(R.string.label_frequency)}: ${freq.toInt()} Hz", value = freq, range = 1f..120f) { freq = it }
                ControlSlider(
                    label = "${stringResource(R.string.label_timebase)}: ${String.format(Locale.US, "%.1f", timebase)} ms",
                    value = timebase,
                    range = 1.0f..20.0f
                ) { timebase = it }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Polarity Toggle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("${stringResource(R.string.hvps_polarity)}: ", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = if (polarity == 1) "(+) ${stringResource(R.string.hvps_pos)}" else "(-) ${stringResource(R.string.hvps_neg)}",
                    color = if (polarity == 1) Color(0xFFE74C3C) else Color(0xFF3498DB),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.width(16.dp))
                Switch(
                    checked = polarity == 1,
                    onCheckedChange = { polarity = if (it) 1 else -1 },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFFE74C3C),
                        uncheckedThumbColor = Color(0xFF3498DB)
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.hvps_type),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

private fun calculateHVPSValue(t: Double, amp: Float, freq: Float, polarity: Int): Float {
    val T = 1.0 / Math.max(1f, freq).toDouble()
    val tLocal = t % T

    // HVPS pulses are very short (e.g. 100 microseconds total for twin peaks)
    // Peak 1: 0-20us, Peak 2: 80-100us (approx representation)
    val p1Start = 0.0
    val p1End = 0.000050 // Adjusted for visual visibility in sim
    val p2Start = 0.000150
    val p2End = 0.000200

    var output = 0f
    if (tLocal >= p1Start && tLocal <= p1End) {
        val mid = (p1Start + p1End) / 2.0
        output = if (tLocal < mid) {
            ((tLocal - p1Start) / (mid - p1Start)).toFloat() * amp
        } else {
            (1.0 - (tLocal - mid) / (p1End - mid)).toFloat() * amp
        }
    } else if (tLocal >= p2Start && tLocal <= p2End) {
        val mid = (p2Start + p2End) / 2.0
        output = if (tLocal < mid) {
            ((tLocal - p2Start) / (mid - p2Start)).toFloat() * amp
        } else {
            (1.0 - (tLocal - mid) / (p2End - mid)).toFloat() * amp
        }
    }

    return output * polarity
}
