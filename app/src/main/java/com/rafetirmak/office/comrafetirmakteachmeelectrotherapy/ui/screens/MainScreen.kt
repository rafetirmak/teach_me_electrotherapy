package com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
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

data class CurrentType(val id: String, val nameResId: Int, val level: DifficultyLevel)

enum class DifficultyLevel(val titleResId: Int) {
    BEGINNER(R.string.level_beginner),
    INTERMEDIATE(R.string.level_intermediate),
    ADVANCED(R.string.level_advanced),
    EXPERT(R.string.level_expert)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToCurrent: (String) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val context = LocalContext.current
    var showExitDialog by remember { mutableStateOf(false) }

    val currentTypes = listOf(
        CurrentType("signal_generator", R.string.current_signal_gen, DifficultyLevel.BEGINNER),
        CurrentType("skin_filter", R.string.current_skin_filter, DifficultyLevel.BEGINNER),
        CurrentType("galvanic", R.string.current_galvanic, DifficultyLevel.INTERMEDIATE),
        CurrentType("diadinamic", R.string.current_diadinamic, DifficultyLevel.INTERMEDIATE),
        CurrentType("tens", R.string.current_tens, DifficultyLevel.ADVANCED),
        CurrentType("faradic", R.string.current_faradic, DifficultyLevel.ADVANCED),
        CurrentType("high_voltage", R.string.current_high_voltage, DifficultyLevel.EXPERT),
        CurrentType("russian", R.string.current_russian, DifficultyLevel.EXPERT),
        CurrentType("ifc", R.string.current_ifc, DifficultyLevel.EXPERT)
    )

    val groupedCurrents = remember(currentTypes) {
        currentTypes.groupBy { it.level }
    }

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
            groupedCurrents.forEach { (level, currents) ->
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = stringResource(level.titleResId),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }

                items(currents) { type ->
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
}
