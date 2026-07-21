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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import androidx.compose.ui.res.stringResource
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.R

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync

data class CurrentType(val id: String, val nameResId: Int, val level: DifficultyLevel)

enum class DifficultyLevel(val titleResId: Int) {
    BEGINNER(R.string.level_beginner),
    INTERMEDIATE(R.string.level_intermediate),
    ADVANCED(R.string.level_advanced),
    EXPERT(R.string.level_expert),
    SPECIALIST(R.string.level_specialist)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToCurrent: (String) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToDictionary: () -> Unit,
    onNavigateToAcknowledgments: () -> Unit,
    onNavigateToDictionarySync: () -> Unit
) {
    val context = LocalContext.current
    var showExitDialog by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }

    val currentTypes = listOf(
        CurrentType("signal_generator", R.string.current_signal_gen, DifficultyLevel.BEGINNER),
        CurrentType("skin_filter", R.string.current_skin_filter, DifficultyLevel.BEGINNER),
        CurrentType("galvanic", R.string.current_galvanic, DifficultyLevel.INTERMEDIATE),
        CurrentType("diadinamic", R.string.current_diadinamic, DifficultyLevel.INTERMEDIATE),
        CurrentType("tens", R.string.current_tens, DifficultyLevel.ADVANCED),
        CurrentType("faradic", R.string.current_faradic, DifficultyLevel.ADVANCED),
        CurrentType("high_voltage", R.string.current_high_voltage, DifficultyLevel.EXPERT),
        CurrentType("russian", R.string.current_russian, DifficultyLevel.EXPERT),
        CurrentType("ifc", R.string.current_ifc, DifficultyLevel.EXPERT),
        CurrentType("semg", R.string.twin_app_title, DifficultyLevel.SPECIALIST)
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
            Surface(shadowElevation = 4.dp) {
                Column(modifier = Modifier.fillMaxWidth().statusBarsPadding()) {
                    Text(
                        text = stringResource(R.string.main_title),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 8.dp, bottom = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onNavigateToDictionary) {
                            Icon(Icons.Default.Book, contentDescription = stringResource(R.string.dictionary_title))
                        }
                        
                        Spacer(modifier = Modifier.weight(1f))
                        
                        Box {
                            IconButton(onClick = { menuExpanded = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                            }
                            DropdownMenu(
                                expanded = menuExpanded,
                                onDismissRequest = { menuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.settings_title)) },
                                    onClick = {
                                        menuExpanded = false
                                        onNavigateToSettings()
                                    },
                                    leadingIcon = { Icon(Icons.Default.Settings, null) }
                                )
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.sync_dictionary)) },
                                    onClick = {
                                        menuExpanded = false
                                        onNavigateToDictionarySync()
                                    },
                                    leadingIcon = { Icon(Icons.Default.Sync, null) }
                                )
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.acknowledgments_title)) },
                                    onClick = {
                                        menuExpanded = false
                                        onNavigateToAcknowledgments()
                                    },
                                    leadingIcon = { Icon(Icons.Default.Star, null) }
                                )
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.about_title)) },
                                    onClick = {
                                        menuExpanded = false
                                        onNavigateToAbout()
                                    },
                                    leadingIcon = { Icon(Icons.Default.Info, null) }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.label_exit)) },
                                    onClick = {
                                        menuExpanded = false
                                        showExitDialog = true
                                    },
                                    leadingIcon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, null) }
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { padding ->
        val uriHandler = LocalUriHandler.current

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(padding)
        ) {
            // Seviye 1 öncesi Eğitim Videoları Bölümü
            item(span = { GridItemSpan(maxLineSpan) }) {
                val youtubeLink = stringResource(R.string.youtube_url)
                Card(
                    onClick = { uriHandler.openUri(youtubeLink) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Book, // Using an available icon
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = stringResource(R.string.label_tutorials),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = stringResource(R.string.desc_tutorials),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }

            groupedCurrents.forEach { (level, currents) ->
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = stringResource(level.titleResId),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }

                if (level == DifficultyLevel.SPECIALIST) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        val semgUrl = "https://play.google.com/store/apps/details?id=com.rafetirmak.teachmesemg"
                        Card(
                            onClick = { uriHandler.openUri(semgUrl) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = stringResource(R.string.twin_app_title),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer
                                    )
                                    Text(
                                        text = stringResource(R.string.twin_app_desc),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer
                                    )
                                }
                            }
                        }
                    }
                } else {
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
}
