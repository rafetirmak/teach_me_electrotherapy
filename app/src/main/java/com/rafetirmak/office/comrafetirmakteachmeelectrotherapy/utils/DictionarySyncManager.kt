package com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.utils

import android.content.Context
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.model.DictionaryData
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.model.DictionaryEntry
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.model.VersionData
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
private data class ServerDictionaryEntry(
    val term_en: String,
    val term_tr: String,
    val definition_en: String,
    val definition_tr: String
)

object DictionarySyncManager {
    private const val BASE_URL = "https://www.rafetirmak.com/assets/data/android_data/teachme_electrotherapy"
    private const val FILE_NAME = "dictionary.json"
    private const val VERSIONS_FILE = "versions.json"
    private const val PREFS_NAME = "dictionary_prefs"
    private const val KEY_DICT_VERSION = "dict_version"

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
    }

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(json)
        }
    }

    /**
     * Checks if a newer version exists on the server and downloads it if necessary.
     */
    suspend fun autoSyncIfNeeded(context: Context) {
        try {
            val timestamp = System.currentTimeMillis()
            val versions: VersionData = client.get("$BASE_URL/$VERSIONS_FILE?t=$timestamp").body()
            val serverVersion = versions.dictionary.version
            
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val localVersion = prefs.getInt(KEY_DICT_VERSION, 0)

            if (serverVersion > localVersion) {
                val success = syncDictionary(context)
                if (success) {
                    prefs.edit().putInt(KEY_DICT_VERSION, serverVersion).apply()
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("SyncError", "Auto sync failed: ${e.message}")
        }
    }

    suspend fun syncDictionary(context: Context): Boolean {
        return try {
            val timestamp = System.currentTimeMillis()
            val response: String = client.get("$BASE_URL/$FILE_NAME?t=$timestamp").body()
            val serverList = json.decodeFromString<List<ServerDictionaryEntry>>(response)
            
            val trList = serverList.map { DictionaryEntry(it.term_tr, it.definition_tr) }
            val enList = serverList.map { DictionaryEntry(it.term_en, it.definition_en) }
            
            val data = DictionaryData(tr = trList, en = enList)
            saveLocal(context, data)
            true
        } catch (e: Exception) {
            android.util.Log.e("SyncError", "Sync failed: ${e.message}")
            false
        }
    }

    suspend fun testConnection(): VersionData? {
        return try {
            val timestamp = System.currentTimeMillis()
            val response = client.get("$BASE_URL/$VERSIONS_FILE?t=$timestamp")
            if (response.status.value == 200) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun manualSync(context: Context, version: Int): Boolean {
        val success = syncDictionary(context)
        if (success) {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit().putInt(KEY_DICT_VERSION, version).apply()
        }
        return success
    }

    private fun saveLocal(context: Context, data: DictionaryData) {
        val file = File(context.filesDir, FILE_NAME)
        val jsonString = json.encodeToString(data)
        file.writeText(jsonString)
    }

    fun getLocalDictionary(context: Context, lang: String): List<DictionaryEntry>? {
        return try {
            val file = File(context.filesDir, FILE_NAME)
            if (file.exists()) {
                val jsonString = file.readText()
                val data = json.decodeFromString<DictionaryData>(jsonString)
                if (lang == "tr") data.tr else data.en
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
