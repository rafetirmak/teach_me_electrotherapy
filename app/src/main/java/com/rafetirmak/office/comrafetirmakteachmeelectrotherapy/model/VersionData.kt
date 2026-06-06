package com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.model

import kotlinx.serialization.Serializable

@Serializable
data class VersionInfo(
    val version: Int,
    val file_path: String,
    val last_updated: String
)

@Serializable
data class VersionData(
    val dictionary: VersionInfo
)
