package com.pouyaheydari.calendar.core.pojo

import android.content.Context
import com.pouyaheydari.calendar.core.R

enum class WeekDay {
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday,
    Sunday;

    val weekDayNumber: Int get() = this.ordinal
    val distanceFromFirstDayOfWeek: Int get() = (this.ordinal + 2) % 7

    fun getName(context: Context): String = when (weekDayNumber) {
        Monday.weekDayNumber -> context.getString(R.string.monday)
        Tuesday.weekDayNumber -> context.getString(R.string.tuesday)
        Wednesday.weekDayNumber -> context.getString(R.string.wednesdays)
        Thursday.weekDayNumber -> context.getString(R.string.thursday)
        Friday.weekDayNumber -> context.getString(R.string.friday)
        Saturday.weekDayNumber -> context.getString(R.string.saturday)
        Sunday.weekDayNumber -> context.getString(R.string.sunday)
        else -> throw IllegalArgumentException("Week day number is grater than 6: $this")
    }
}