package com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.ui.components.ControlSlider
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.ui.components.OscilloscopeView
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.ui.components.WaveformChannel

import androidx.compose.ui.res.stringResource
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.R

enum class TensMode(val labelResId: Int) {
    BIPHASIC_SYM(R.string.tens_mode_sym),
    BIPHASIC_ASYM(R.string.tens_mode_asym),
    MONOPHASIC(R.string.tens_mode_mono),
    BURST(R.string.tens_mode_burst)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TensScreen(onBack: () -> Unit) {
    var amp by remember { mutableFloatStateOf(20f) }
    var freq by remember { mutableFloatStateOf(80f) }
    var pw by remember { mutableFloatStateOf(200f) }
    var timebase by remember { mutableFloatStateOf(2.0f) }
    var selectedMode by remember { mutableStateOf(TensMode.BIPHASIC_SYM) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.tens_title)) },
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
                        name = "TENS",
                        color = Color(0xFF2980B9),
                        onDrawWaveform = { t ->
                            calculateTensValue(t, amp, freq, pw, selectedMode)
                        }
                    )
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Controls
            Column(modifier = Modifier.fillMaxWidth()) {
                ControlSlider(label = "${stringResource(R.string.label_amplitude)}: ${amp.toInt()}", value = amp, range = 0f..100f) { amp = it }
                ControlSlider(label = "${stringResource(R.string.label_frequency)}: ${freq.toInt()} Hz", value = freq, range = 1f..150f) { freq = it }
                ControlSlider(label = "${stringResource(R.string.tens_pulse_width)}: ${pw.toInt()} µs", value = pw, range = 50f..400f) { pw = it }
                ControlSlider(label = "${stringResource(R.string.label_zoom)}: ${String.format("%.1f", timebase)} ms", value = timebase, range = 0.1f..20f) { timebase = it }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mode selection
            Text(stringResource(R.string.tens_mode_selection), style = MaterialTheme.typography.titleSmall)
            Column(Modifier.selectableGroup()) {
                TensMode.values().forEach { mode ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .selectable(
                                selected = (mode == selectedMode),
                                onClick = { selectedMode = mode },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (mode == selectedMode),
                            onClick = null // null recommended for accessibility with selectable modifier
                        )
                        Text(
                            text = stringResource(mode.labelResId),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun calculateTensValue(t: Double, amp: Float, freq: Float, pw: Float, mode: TensMode): Float {
    val T = 1.0 / Math.max(1f, freq).toDouble()
    
    var burstEnvelope = 1f
    if (mode == TensMode.BURST) {
        val burstFreq = 2.0
        val burstPeriod = 1.0 / burstFreq
        val burstPhase = t % burstPeriod
        if (burstPhase > (burstPeriod * 0.5)) burstEnvelope = 0f
    }

    val pwSec = pw / 1000000.0
    val ipiSec = 50.0 / 1000000.0 // ipi is 50 in JS
    val tLocal = t % T

    var output = 0f
    if (tLocal < pwSec) {
        output = amp
    } else if (tLocal < (pwSec + ipiSec)) {
        output = 0f
    } else if (tLocal < (2.0 * pwSec + ipiSec)) {
        when (mode) {
            TensMode.MONOPHASIC -> output = 0f
            TensMode.BIPHASIC_ASYM -> output = -amp * 0.6f
            else -> output = -amp
        }
    } else {
        output = 0f
    }

    return output * burstEnvelope
}
