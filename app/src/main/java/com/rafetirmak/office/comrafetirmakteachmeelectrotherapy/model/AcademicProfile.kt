package com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.model

import androidx.annotation.StringRes

data class AcademicProfile(
    @StringRes val nameRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val bioRes: Int,
    val websiteUrl: String,
    val linkedInUrl: String,
    val orcidId: String,
    val wosId: String,
    val books: List<FizyoBook>
)

data class FizyoBook(
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
    val storeUrl: String,
    val imageUrl: String? = null
)

data class ScientificPublication(
    val title: String,
    val journal: String,
    val year: String,
    val doiUrl: String
)
