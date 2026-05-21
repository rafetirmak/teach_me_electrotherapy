package com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import kotlin.math.PI
import kotlin.math.sin
import kotlin.math.sign
import kotlin.math.floor

import androidx.compose.ui.res.stringResource
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.R

enum class WaveformType(val labelResId: Int) {
    SINE(R.string.sig_gen_sine),
    SQUARE(R.string.sig_gen_square),
    SAWTOOTH(R.string.sig_gen_sawtooth)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignalGeneratorScreen(onBack: () -> Unit) {
    var amp by remember { mutableFloatStateOf(50f) }
    var freq by remember { mutableFloatStateOf(50f) }
    var selectedWaveform by remember { mutableStateOf(WaveformType.SINE) }
    var timebase by remember { mutableFloatStateOf(10.0f) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.sig_gen_title)) },
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
                        name = "Generator",
                        color = Color(0xFF2980B9),
                        expectedMaxFreq = freq,
                        onDrawWaveform = { t ->
                            calculateWaveformValue(t, amp, freq, selectedWaveform)
                        }
                    )
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Controls
            Column(modifier = Modifier.fillMaxWidth()) {
                ControlSlider(label = "${stringResource(R.string.label_amplitude)}: ${amp.toInt()} V", value = amp, range = 10f..100f) { amp = it }
                ControlSlider(label = "${stringResource(R.string.label_frequency)}: ${freq.toInt()} Hz", value = freq, range = 1f..20f) { freq = it }
                ControlSlider(label = "${stringResource(R.string.label_zoom)}: ${timebase.toInt()} ms", value = timebase, range = 1f..100f) { timebase = it }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Waveform selection
            Text(stringResource(R.string.sig_gen_waveform), style = MaterialTheme.typography.titleSmall)
            Column(Modifier.selectableGroup()) {
                WaveformType.values().forEach { type ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .selectable(
                                selected = (type == selectedWaveform),
                                onClick = { selectedWaveform = type },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (type == selectedWaveform),
                            onClick = null
                        )
                        Text(
                            text = stringResource(type.labelResId),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun calculateWaveformValue(t: Double, amp: Float, freq: Float, type: WaveformType): Float {
    return when (type) {
        WaveformType.SINE -> (sin(2.0 * PI * freq * t) * amp).toFloat()
        WaveformType.SQUARE -> (sign(sin(2.0 * PI * freq * t)) * amp).toFloat()
        WaveformType.SAWTOOTH -> {
            val period = 1.0 / freq
            val tLocal = t % period
            (2.0 * (tLocal / period - 0.5) * amp).toFloat()
        }
    }
}
