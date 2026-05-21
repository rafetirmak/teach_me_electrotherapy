package com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.model

import kotlinx.serialization.Serializable

@Serializable
data class DictionaryEntry(
    val term: String,
    val definition: String
)
