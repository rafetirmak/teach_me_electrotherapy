package com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
fun GalvanicScreen(onBack: () -> Unit) {
    var amp by remember { mutableFloatStateOf(25f) }
    var dutyCycle by remember { mutableFloatStateOf(75f) }
    var timebase by remember { mutableFloatStateOf(5.0f) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.galvanic_title)) },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.galvanic_direct), style = MaterialTheme.typography.titleSmall)
            OscilloscopeView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                timebaseMs = timebase,
                channels = listOf(
                    WaveformChannel(
                        name = "Direct",
                        color = Color(0xFF2980B9),
                        onDrawWaveform = { _ -> amp }
                    )
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
            
            Text(stringResource(R.string.galvanic_interrupted), style = MaterialTheme.typography.titleSmall)
            OscilloscopeView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                timebaseMs = timebase,
                channels = listOf(
                    WaveformChannel(
                        name = "Interrupted",
                        color = Color(0xFF27AE60),
                        onDrawWaveform = { t ->
                            val period = 0.1 // 100ms equivalent in time domain for visualization
                            if ((t % period) < (period * 0.5)) amp else 0f
                        }
                    )
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(stringResource(R.string.galvanic_variable), style = MaterialTheme.typography.titleSmall)
            OscilloscopeView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                timebaseMs = timebase,
                channels = listOf(
                    WaveformChannel(
                        name = "Variable",
                        color = Color(0xFFE67E22),
                        onDrawWaveform = { t ->
                            val period = 0.1
                            if ((t % period) < (period * (dutyCycle / 100f))) amp else 0f
                        }
                    )
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Controls
            Column(modifier = Modifier.fillMaxWidth()) {
                ControlSlider(label = "${stringResource(R.string.label_amplitude)}: ${amp.toInt()} V", value = amp, range = 1f..50f) { amp = it }
                ControlSlider(label = "${stringResource(R.string.galvanic_duty_cycle)}: %${dutyCycle.toInt()}", value = dutyCycle, range = 10f..90f) { dutyCycle = it }
                ControlSlider(label = "${stringResource(R.string.label_timebase)}: ${String.format("%.1f", timebase)} ms", value = timebase, range = 1.0f..20f) { timebase = it }
            }
        }
    }
}
