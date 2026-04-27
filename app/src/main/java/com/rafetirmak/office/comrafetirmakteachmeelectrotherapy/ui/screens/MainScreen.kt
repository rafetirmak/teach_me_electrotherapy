package com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import androidx.compose.ui.res.stringResource
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.R

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings

data class CurrentType(val id: String, val nameResId: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToCurrent: (String) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val context = LocalContext.current
    var showExitDialog by remember { mutableStateOf(false) }

    val currentTypes = listOf(
        CurrentType("tens", R.string.current_tens),
        CurrentType("ifc", R.string.current_ifc),
        CurrentType("faradic", R.string.current_faradic),
        CurrentType("russian", R.string.current_russian),
        CurrentType("galvanic", R.string.current_galvanic),
        CurrentType("diadinamic", R.string.current_diadinamic),
        CurrentType("high_voltage", R.string.current_high_voltage),
        CurrentType("signal_generator", R.string.current_signal_gen),
        CurrentType("skin_filter", R.string.current_skin_filter)
    )

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text(stringResource(R.string.label_exit)) },
            text = { Text(stringResource(R.string.exit_confirm)) },
            confirmButton = {
                TextButton(onClick = { (context as? Activity)?.finish() }) {
                    Text(stringResource(R.string.label_yes))
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text(stringResource(R.string.label_no))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.main_title)) },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.settings_title))
                    }
                    IconButton(onClick = { showExitDialog = true }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = stringResource(R.string.label_exit))
                    }
                }
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(padding)
        ) {
            items(currentTypes) { type ->
                Card(
                    onClick = { onNavigateToCurrent(type.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = stringResource(type.nameResId),
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}
