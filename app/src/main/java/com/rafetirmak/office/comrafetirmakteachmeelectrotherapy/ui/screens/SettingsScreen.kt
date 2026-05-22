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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.R
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.utils.SettingsManager
import androidx.activity.ComponentActivity
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.utils.DictionarySyncManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val settingsManager = remember { SettingsManager(context) }
    var selectedLanguage by remember { mutableStateOf(settingsManager.language) }
    
    var connectionStatus by remember { mutableStateOf<String?>(null) }
    var foundFiles by remember { mutableStateOf<List<String>>(emptyList()) }
    var isChecking by remember { mutableStateOf(false) }
    var isDownloading by remember { mutableStateOf(false) }
    var downloadMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
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
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.settings_language),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            val languages = listOf(
                "tr" to stringResource(R.string.lang_tr),
                "en" to stringResource(R.string.lang_en)
            )

            Column(Modifier.selectableGroup()) {
                languages.forEach { (code, label) ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (code == selectedLanguage),
                                onClick = {
                                    if (code != selectedLanguage) {
                                        selectedLanguage = code
                                        settingsManager.language = code
                                        (context as? ComponentActivity)?.recreate()
                                    }
                                },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (code == selectedLanguage),
                            onClick = null
                        )
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.sync_dictionary),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Button(
                onClick = {
                    isChecking = true
                    connectionStatus = null
                    downloadMessage = null
                    scope.launch {
                        val files = DictionarySyncManager.testConnection()
                        isChecking = false
                        if (files != null) {
                            connectionStatus = context.getString(R.string.connection_ok)
                            foundFiles = files
                        } else {
                            connectionStatus = context.getString(R.string.connection_error)
                            foundFiles = emptyList()
                        }
                    }
                },
                enabled = !isChecking && !isDownloading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isChecking) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(stringResource(R.string.check_connection))
                }
            }

            if (connectionStatus != null) {
                Text(
                    text = connectionStatus!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (foundFiles.isNotEmpty()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (foundFiles.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.found_files),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 16.dp)
                )
                foundFiles.forEach { fileName ->
                    Text(
                        text = "- $fileName",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        isDownloading = true
                        downloadMessage = null
                        scope.launch {
                            val success = DictionarySyncManager.syncDictionary(context)
                            isDownloading = false
                            downloadMessage = if (success) {
                                context.getString(R.string.download_success)
                            } else {
                                context.getString(R.string.download_error)
                            }
                        }
                    },
                    enabled = !isDownloading,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    if (isDownloading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onSecondary)
                    } else {
                        Text(stringResource(R.string.confirm_and_download))
                    }
                }
            }

            if (downloadMessage != null) {
                Text(
                    text = downloadMessage!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (downloadMessage == context.getString(R.string.download_success)) 
                        MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
