package com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.model

import kotlinx.serialization.Serializable

@Serializable
data class DictionaryData(
    val tr: List<DictionaryEntry>,
    val en: List<DictionaryEntry>
)
