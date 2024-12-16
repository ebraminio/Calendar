package com.pouyaheydari.calendar.core.pojo

import android.content.Context
import androidx.annotation.StringRes
import com.pouyaheydari.calendar.core.R

enum class ShamsiMonths(@StringRes private val nameStringId: Int) {
    Farwarding(R.string.farvardin),
    Ordibehesht(R.string.ordibehesht),
    Khordad(R.string.khordad),
    Tir(R.string.tir),
    Mordad(R.string.mordad),
    Shahrivar(R.string.shahrivar),
    Mehr(R.string.mehr),
    Aban(R.string.aban),
    Azar(R.string.azar),
    Dey(R.string.dey),
    Bahman(R.string.bahman),
    Esfand(R.string.esfand);

    val monthNumber: Int get() = this.ordinal + 1

    fun getName(context: Context): String = context.getString(this.nameStringId)
}
