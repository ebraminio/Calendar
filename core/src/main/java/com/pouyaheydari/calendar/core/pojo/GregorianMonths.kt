package com.pouyaheydari.calendar.core.pojo

import android.content.Context
import com.pouyaheydari.calendar.core.R

enum class GregorianMonths {
    January,
    February,
    March,
    April,
    May,
    Jun,
    July,
    August,
    September,
    October,
    November,
    December;

    val monthNumber: Int get() = this.ordinal + 1

    fun getName(context: Context): String = when (monthNumber) {
        January.monthNumber -> context.getString(R.string.january)
        February.monthNumber -> context.getString(R.string.february)
        March.monthNumber -> context.getString(R.string.march)
        April.monthNumber -> context.getString(R.string.april)
        May.monthNumber -> context.getString(R.string.may)
        Jun.monthNumber -> context.getString(R.string.jun)
        July.monthNumber -> context.getString(R.string.july)
        August.monthNumber -> context.getString(R.string.august)
        September.monthNumber -> context.getString(R.string.september)
        October.monthNumber -> context.getString(R.string.october)
        November.monthNumber -> context.getString(R.string.november)
        December.monthNumber -> context.getString(R.string.december)
        else -> throw IllegalArgumentException("Month number is grater than 12")
    }
}
