package com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.ui.components.ControlSlider
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.ui.components.OscilloscopeView
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.ui.components.WaveformChannel

import androidx.compose.ui.res.stringResource
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaradicScreen(onBack: () -> Unit) {
    var amp by remember { mutableFloatStateOf(40f) }
    var pulseDuration by remember { mutableFloatStateOf(1f) } // ms
    var pulseInterval by remember { mutableFloatStateOf(20f) } // ms
    var surgeOn by remember { mutableFloatStateOf(2f) } // s
    var surgeOff by remember { mutableFloatStateOf(3f) } // s
    var timebase by remember { mutableFloatStateOf(100f) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.faradic_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.label_back))
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
                        name = stringResource(R.string.current_faradic),
                        color = Color(0xFFE67E22),
                        onDrawWaveform = { t ->
                            calculateFaradicValue(t, amp, pulseDuration, pulseInterval, surgeOn, surgeOff)
                        }
                    )
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Controls
            Column(modifier = Modifier.fillMaxWidth()) {
                ControlSlider(label = "${stringResource(R.string.label_amplitude)}: ${amp.toInt()} mA", value = amp, range = 0f..100f) { amp = it }
                ControlSlider(label = "${stringResource(R.string.tens_pulse_width)}: $pulseDuration ms", value = pulseDuration, range = 0.1f..10f) { pulseDuration = it }
                ControlSlider(label = "${stringResource(R.string.ifc_amf)}: $pulseInterval ms", value = pulseInterval, range = 5f..100f) { pulseInterval = it }
                ControlSlider(label = "${stringResource(R.string.faradic_on)}: $surgeOn s", value = surgeOn, range = 1f..10f) { surgeOn = it }
                ControlSlider(label = "${stringResource(R.string.faradic_off)}: $surgeOff s", value = surgeOff, range = 1f..10f) { surgeOff = it }
                ControlSlider(label = "${stringResource(R.string.label_zoom)}: ${timebase.toInt()} ms", value = timebase, range = 10f..1000f) { timebase = it }
            }
        }
    }
}

private fun calculateFaradicValue(
    t: Double,
    amp: Float,
    pulseDuration: Float,
    pulseInterval: Float,
    surgeOn: Float,
    surgeOff: Float
): Float {
    val totalSurgePeriod = (surgeOn + surgeOff).toDouble()
    val tSurge = t % totalSurgePeriod
    
    // Surge envelope (ramp up/down can be added here)
    val envelope = if (tSurge < surgeOn) {
        // Simple ramp or sustain
        1.0f
    } else {
        0.0f
    }

    val pulsePeriodSec = (pulseDuration + pulseInterval) / 1000.0
    val tPulse = t % pulsePeriodSec
    val pulseDurationSec = pulseDuration / 1000.0

    return if (tPulse < pulseDurationSec) {
        amp * envelope
    } else {
        0f
    }
}
