package com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.R
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.ui.components.ControlSlider
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.ui.components.OscilloscopeView
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.ui.components.WaveformChannel
import java.util.Locale
import kotlin.math.PI
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkinFilterScreen(onBack: () -> Unit) {
    var amp by remember { mutableFloatStateOf(60f) }
    var freq by remember { mutableFloatStateOf(50f) }
    var isSkinFilterOn by remember { mutableStateOf(true) }
    var timebase by remember { mutableFloatStateOf(10.0f) }

    val transmissionFactor = if (isSkinFilterOn) {
        // Simple impedance model: frequency / (frequency + k)
        val k = 1000f
        freq / (freq + k)
    } else {
        1.0f
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.skin_title)) },
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
                        name = stringResource(R.string.skin_output),
                        color = Color(0xFF00D2FF).copy(alpha = 0.3f),
                        onDrawWaveform = { t -> (sin(2.0 * PI * freq * t) * amp).toFloat() }
                    ),
                    WaveformChannel(
                        name = stringResource(R.string.skin_in_tissue),
                        color = if (isSkinFilterOn) Color(0xFFFF3838) else Color(0xFF00D2FF),
                        onDrawWaveform = { t -> (sin(2.0 * PI * freq * t) * amp * transmissionFactor).toFloat() }
                    )
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.skin_model), style = MaterialTheme.typography.titleMedium)
                Switch(checked = isSkinFilterOn, onCheckedChange = { isSkinFilterOn = it })
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Controls
            Column(modifier = Modifier.fillMaxWidth()) {
                ControlSlider(label = "${stringResource(R.string.label_frequency)}: ${freq.toInt()} Hz", value = freq, range = 10f..4000f) { freq = it }
                ControlSlider(label = "${stringResource(R.string.label_amplitude)}: ${amp.toInt()} V", value = amp, range = 10f..100f) { amp = it }
                ControlSlider(
                    label = "${stringResource(R.string.label_zoom)}: ${String.format(Locale.US, "%.1f", timebase)} ms",
                    value = timebase,
                    range = 1.0f..50.0f
                ) { timebase = it }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "${stringResource(R.string.skin_transmission)}: %${String.format(Locale.US, "%.1f", transmissionFactor * 100)}",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFFFF3838)
                    )
                    Text(
                        stringResource(R.string.skin_desc),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
