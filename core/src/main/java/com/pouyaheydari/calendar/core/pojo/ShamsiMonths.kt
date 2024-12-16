package com.pouyaheydari.calendar.core.pojo

import android.content.Context
import com.pouyaheydari.calendar.core.R

enum class ShamsiMonths {
    Farwarding,
    Ordibehesht,
    Khordad,
    Tir,
    Mordad,
    Shahrivar,
    Mehr,
    Aban,
    Azar,
    Dey,
    Bahman,
    Esfand;

    val monthNumber: Int get() = this.ordinal + 1

    fun getName(context: Context): String = when (monthNumber) {
        Farwarding.monthNumber -> context.getString(R.string.farvardin)
        Ordibehesht.monthNumber -> context.getString(R.string.ordibehesht)
        Khordad.monthNumber -> context.getString(R.string.khordad)
        Tir.monthNumber -> context.getString(R.string.tir)
        Mordad.monthNumber -> context.getString(R.string.mordad)
        Shahrivar.monthNumber -> context.getString(R.string.shahrivar)
        Mehr.monthNumber -> context.getString(R.string.mehr)
        Aban.monthNumber -> context.getString(R.string.aban)
        Azar.monthNumber -> context.getString(R.string.azar)
        Dey.monthNumber -> context.getString(R.string.dey)
        Bahman.monthNumber -> context.getString(R.string.bahman)
        Esfand.monthNumber -> context.getString(R.string.esfand)
        else -> throw IllegalArgumentException("Month number is grater than 12: $this")
    }
}
