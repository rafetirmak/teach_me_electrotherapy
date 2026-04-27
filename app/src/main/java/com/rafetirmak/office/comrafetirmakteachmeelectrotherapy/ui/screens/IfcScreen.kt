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
fun IfcScreen(onBack: () -> Unit) {
    var amp by remember { mutableFloatStateOf(50f) }
    var amf by remember { mutableFloatStateOf(100f) }
    var timebase by remember { mutableFloatStateOf(10.0f) }
    var autoSync by remember { mutableStateOf(true) }

    val carrier = 4000f

    LaunchedEffect(amf, autoSync) {
        if (autoSync) {
            val idealTB = (2000f / amf) / 10f
            timebase = Math.max(0.1f, Math.min(20f, idealTB))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.ifc_title)) },
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
            val channels = listOf(
                WaveformChannel(
                    name = stringResource(R.string.ifc_carrier),
                    color = Color(0xFF2980B9),
                    centerYPercent = 0.2f,
                    scaleY = amp * 0.8f * 0.5f, // Scaled down to fit
                    onDrawWaveform = { t -> Math.sin(2.0 * Math.PI * carrier * t).toFloat() }
                ),
                WaveformChannel(
                    name = stringResource(R.string.ifc_modulator),
                    color = Color(0xFFE67E22),
                    centerYPercent = 0.5f,
                    scaleY = amp * 0.8f * 0.5f,
                    onDrawWaveform = { t -> Math.sin(2.0 * Math.PI * amf * t).toFloat() }
                ),
                WaveformChannel(
                    name = stringResource(R.string.ifc_result),
                    color = Color(0xFF27AE60),
                    centerYPercent = 0.8f,
                    scaleY = amp * 0.4f * 0.5f,
                    onDrawWaveform = { t ->
                        val wave1 = Math.sin(2.0 * Math.PI * carrier * t)
                        val wave2 = Math.sin(2.0 * Math.PI * (carrier + amf) * t)
                        (wave1 + wave2).toFloat()
                    }
                )
            )

            OscilloscopeView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                timebaseMs = timebase,
                channels = channels
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                ControlSlider(label = "${stringResource(R.string.label_amplitude)}: ${amp.toInt()} mA", value = amp, range = 0f..100f) { amp = it }
                ControlSlider(label = "${stringResource(R.string.ifc_amf)}: ${amf.toInt()} Hz", value = amf, range = 1f..250f) { amf = it }
                ControlSlider(label = "${stringResource(R.string.label_zoom)}: ${String.format("%.1f", timebase)} ms", value = timebase, range = 0.1f..20f) { timebase = it }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = autoSync, onCheckedChange = { autoSync = it })
                    Text(stringResource(R.string.ifc_autosync))
                }
            }
        }
    }
}
