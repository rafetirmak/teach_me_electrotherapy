package com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.R
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.utils.DictionarySyncManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionarySyncScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var connectionStatus by remember { mutableStateOf<String?>(null) }
    var serverVersionInfo by remember { mutableStateOf<com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.model.VersionData?>(null) }
    var isChecking by remember { mutableStateOf(false) }
    var isDownloading by remember { mutableStateOf(false) }
    var downloadMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.sync_dictionary)) },
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
                .padding(16.dp)
        ) {
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
                        val versionData = DictionarySyncManager.testConnection()
                        isChecking = false
                        if (versionData != null) {
                            connectionStatus = context.getString(R.string.connection_ok)
                            serverVersionInfo = versionData
                        } else {
                            connectionStatus = context.getString(R.string.connection_error)
                            serverVersionInfo = null
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
                    color = if (serverVersionInfo != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            serverVersionInfo?.let { info ->
                Text(
                    text = stringResource(R.string.found_files),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = "- ${info.dictionary.file_path} (v${info.dictionary.version})",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        isDownloading = true
                        downloadMessage = null
                        scope.launch {
                            val success = DictionarySyncManager.manualSync(context, info.dictionary.version)
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
