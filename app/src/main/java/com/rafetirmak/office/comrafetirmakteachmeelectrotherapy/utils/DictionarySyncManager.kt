package com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.utils

import android.content.Context
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.model.DictionaryEntry
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

object DictionarySyncManager {
    private const val BASE_URL = "https://rafetirmak.com/android_data"
    private const val FILE_TR = "dictionary_tr.json"
    private const val FILE_EN = "dictionary_en.json"

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    suspend fun syncDictionary(context: Context) {
        try {
            val trData: List<DictionaryEntry> = client.get("$BASE_URL/$FILE_TR").body()
            saveLocal(context, "tr", trData)

            val enData: List<DictionaryEntry> = client.get("$BASE_URL/$FILE_EN").body()
            saveLocal(context, "en", enData)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun testConnection(): List<String>? {
        return try {
            val responseTr = client.get("$BASE_URL/$FILE_TR")
            val responseEn = client.get("$BASE_URL/$FILE_EN")
            
            if (responseTr.status.value == 200 && responseEn.status.value == 200) {
                listOf(FILE_TR, FILE_EN)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun saveLocal(context: Context, lang: String, data: List<DictionaryEntry>) {
        val file = File(context.filesDir, "dict_$lang.json")
        val jsonString = Json.encodeToString<List<DictionaryEntry>>(data)
        file.writeText(jsonString)
    }

    fun getLocalDictionary(context: Context, lang: String): List<DictionaryEntry>? {
        return try {
            val file = File(context.filesDir, "dict_$lang.json")
            if (file.exists()) {
                val jsonString = file.readText()
                Json.decodeFromString<List<DictionaryEntry>>(jsonString)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
