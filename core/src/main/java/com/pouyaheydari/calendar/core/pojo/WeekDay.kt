package com.pouyaheydari.calendar.core.pojo

import android.content.Context
import androidx.annotation.StringRes
import com.pouyaheydari.calendar.core.R

enum class WeekDay(@StringRes private val nameStringId: Int) {
    Monday(R.string.monday),
    Tuesday(R.string.tuesday),
    Wednesday(R.string.wednesdays),
    Thursday(R.string.thursday),
    Friday(R.string.friday),
    Saturday(R.string.saturday),
    Sunday(R.string.sunday);

    val weekDayNumber: Int get() = this.ordinal
    val distanceFromFirstDayOfWeek: Int get() = (this.ordinal + 2) % 7

    fun getName(context: Context): String = context.getString(this.nameStringId)
}