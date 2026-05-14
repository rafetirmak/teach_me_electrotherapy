package com.rafetirmak.office.comrafetirmakteachmeelectrotherapy

import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.model.AcademicProfile
import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.model.FizyoBook

object AboutDeveloper {
    fun getProfile(): AcademicProfile {
        return AcademicProfile(
            nameRes = R.string.profile_name,
            titleRes = R.string.profile_title,
            bioRes = R.string.profile_bio,
            imageRes = R.drawable.profile_ri_25,
            websiteUrl = "https://www.rafetirmak.com",
            linkedInUrl = "https://linkedin.com/in/rafet-irmak-4211bb70",
            orcidId = "0000-0003-0409-7535",
            wosId = "C-3357-2012",
            books = listOf(
                FizyoBook(
                    titleRes = R.string.book1_title,
                    descriptionRes = R.string.book1_desc,
                    storeUrl = "https://example.com/kitap1",
                    imageRes = R.drawable.pt_e_kitap_logo
                ),
                FizyoBook(
                    titleRes = R.string.book2_title,
                    descriptionRes = R.string.book2_desc,
                    storeUrl = "https://example.com/kitap2",
                    imageRes = R.drawable.pt_e_kitap_logo
                )
            )
        )
    }
}
