package com.rafetirmak.office.comrafetirmakteachmeelectrotherapy

import com.rafetirmak.office.comrafetirmakteachmeelectrotherapy.R

data class Contributor(
    val nameRes: Int
)

object AcknowledgmentsDataProvider {
    fun getContributors(): List<Contributor> {
        return listOf(
            Contributor(R.string.contrib_alex_ward),
            Contributor(R.string.contrib_tim_watson)
        )
    }
}
