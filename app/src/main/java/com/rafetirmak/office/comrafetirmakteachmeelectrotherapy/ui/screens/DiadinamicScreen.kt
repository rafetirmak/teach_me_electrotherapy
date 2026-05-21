package com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import kotlin.math.abs
import kotlin.math.sin

import androidx.compose.ui.res.stringResource
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiadinamicScreen(onBack: () -> Unit) {
    var amp by remember { mutableFloatStateOf(30f) }
    var timebase by remember { mutableFloatStateOf(10.0f) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.diadinamic_title)) },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.diadinamic_ref), style = MaterialTheme.typography.titleSmall)
            OscilloscopeView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                timebaseMs = timebase,
                channels = listOf(
                    WaveformChannel(
                        name = "Reference",
                        color = Color.Gray,
                        onDrawWaveform = { t ->
                            (sin(2.0 * Math.PI * 50.0 * t) * amp).toFloat()
                        }
                    )
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(stringResource(R.string.diadinamic_mf), style = MaterialTheme.typography.titleSmall)
            OscilloscopeView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                timebaseMs = timebase,
                channels = listOf(
                    WaveformChannel(
                        name = "MF",
                        color = Color(0xFF2980B9),
                        onDrawWaveform = { t ->
                            val s = sin(2.0 * Math.PI * 50.0 * t)
                            (kotlin.math.max(0.0, s) * amp).toFloat()
                        }
                    )
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(stringResource(R.string.diadinamic_df), style = MaterialTheme.typography.titleSmall)
            OscilloscopeView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                timebaseMs = timebase,
                channels = listOf(
                    WaveformChannel(
                        name = "DF",
                        color = Color(0xFF27AE60),
                        onDrawWaveform = { t ->
                            val s = sin(2.0 * Math.PI * 50.0 * t)
                            (abs(s) * amp).toFloat()
                        }
                    )
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Controls
            Column(modifier = Modifier.fillMaxWidth()) {
                ControlSlider(label = "${stringResource(R.string.label_amplitude)}: ${amp.toInt()} V", value = amp, range = 1f..50f) { amp = it }
                ControlSlider(
                    label = "${stringResource(R.string.label_timebase)}: ${String.format(Locale.US, "%.1f", timebase)} ms",
                    value = timebase,
                    range = 5.0f..50.0f
                ) { timebase = it }
            }
        }
    }
}
