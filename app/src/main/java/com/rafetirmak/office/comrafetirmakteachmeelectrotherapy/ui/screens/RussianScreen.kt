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
fun RussianScreen(onBack: () -> Unit) {
    var amp by remember { mutableFloatStateOf(50f) }
    var carrierFreq by remember { mutableFloatStateOf(2500f) } // 2500 Hz standard
    var burstFreq by remember { mutableFloatStateOf(50f) } // 50 Hz standard
    var dutyCycle by remember { mutableFloatStateOf(50f) } // 50% standard
    var surgeOn by remember { mutableFloatStateOf(10f) }
    var surgeOff by remember { mutableFloatStateOf(10f) }
    var timebase by remember { mutableFloatStateOf(20f) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.russian_title)) },
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
                        name = stringResource(R.string.current_russian),
                        color = Color(0xFFC0392B),
                        onDrawWaveform = { t ->
                            calculateRussianValue(t, amp, carrierFreq, burstFreq, dutyCycle, surgeOn, surgeOff)
                        }
                    )
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Controls
            Column(modifier = Modifier.fillMaxWidth()) {
                ControlSlider(label = "${stringResource(R.string.label_amplitude)}: ${amp.toInt()} mA", value = amp, range = 0f..100f) { amp = it }
                ControlSlider(label = "${stringResource(R.string.label_frequency)}: ${carrierFreq.toInt()} Hz", value = carrierFreq, range = 1000f..5000f) { carrierFreq = it }
                ControlSlider(label = "${stringResource(R.string.russian_burst_freq)}: ${burstFreq.toInt()} Hz", value = burstFreq, range = 1f..150f) { burstFreq = it }
                ControlSlider(label = "${stringResource(R.string.galvanic_duty_cycle)}: ${dutyCycle.toInt()}%", value = dutyCycle, range = 10f..100f) { dutyCycle = it }
                ControlSlider(label = "${stringResource(R.string.faradic_on)}: ${surgeOn.toInt()} s", value = surgeOn, range = 1f..30f) { surgeOn = it }
                ControlSlider(label = "${stringResource(R.string.faradic_off)}: ${surgeOff.toInt()} s", value = surgeOff, range = 1f..30f) { surgeOff = it }
                ControlSlider(label = "${stringResource(R.string.label_zoom)}: ${String.format("%.1f", timebase)} ms", value = timebase, range = 0.5f..100f) { timebase = it }
            }
        }
    }
}

private fun calculateRussianValue(
    t: Double,
    amp: Float,
    carrierFreq: Float,
    burstFreq: Float,
    dutyCycle: Float,
    surgeOn: Float,
    surgeOff: Float
): Float {
    val totalSurgePeriod = (surgeOn + surgeOff).toDouble()
    val tSurge = t % totalSurgePeriod
    if (tSurge > surgeOn) return 0f

    val burstPeriod = 1.0 / burstFreq
    val tBurst = t % burstPeriod
    val burstOnDuration = burstPeriod * (dutyCycle / 100.0)
    
    if (tBurst < burstOnDuration) {
        return (amp * Math.sin(2.0 * Math.PI * carrierFreq * t)).toFloat()
    } else {
        return 0f
    }
}
