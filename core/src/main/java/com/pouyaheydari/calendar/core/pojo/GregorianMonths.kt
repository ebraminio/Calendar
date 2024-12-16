package com.pouyaheydari.calendar.core.pojo

import android.content.Context
import androidx.annotation.StringRes
import com.pouyaheydari.calendar.core.R

enum class GregorianMonths(@StringRes private val nameStringId: Int) {
    January(R.string.january),
    February(R.string.february),
    March(R.string.march),
    April(R.string.april),
    May(R.string.may),
    Jun(R.string.jun),
    July(R.string.july),
    August(R.string.august),
    September(R.string.september),
    October(R.string.october),
    November(R.string.november),
    December(R.string.december);

    val monthNumber: Int get() = this.ordinal + 1

    fun getName(context: Context): String = context.getString(this.nameStringId)
}
