package com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.utils

import android.content.Context
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.model.DictionaryData
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.model.DictionaryEntry
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
    private const val BASE_URL = "https://rafetirmak.com/assets/data/android_data/teachme_electrotherapy"
    private const val FILE_NAME = "dictionary.json"

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

    suspend fun syncDictionary(context: Context): Boolean {
        return try {
            val response: String = client.get("$BASE_URL/$FILE_NAME").body()
            // Sunucudan gelen listeyi oku
            val serverList = json.decodeFromString<List<ServerDictionaryEntry>>(response)
            
            // Veriyi uygulamanın iç formatına (tr/en listeleri) dönüştür
            val trList = serverList.map { DictionaryEntry(it.term_tr, it.definition_tr) }
            val enList = serverList.map { DictionaryEntry(it.term_en, it.definition_en) }
            
            val data = DictionaryData(tr = trList, en = enList)
            saveLocal(context, data)
            true
        } catch (e: Exception) {
            android.util.Log.e("SyncError", "Hata: ${e.message}")
            e.printStackTrace()
            false
        }
    }

    suspend fun testConnection(): List<String>? {
        return try {
            val response = client.get("$BASE_URL/$FILE_NAME")
            if (response.status.value == 200) {
                listOf(FILE_NAME)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun saveLocal(context: Context, data: DictionaryData) {
        val file = File(context.filesDir, FILE_NAME)
        val jsonString = Json.encodeToString(data)
        file.writeText(jsonString)
    }

    fun getLocalDictionary(context: Context, lang: String): List<DictionaryEntry>? {
        return try {
            val file = File(context.filesDir, FILE_NAME)
            if (file.exists()) {
                val jsonString = file.readText()
                val data = Json.decodeFromString<DictionaryData>(jsonString)
                if (lang == "tr") data.tr else data.en
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
