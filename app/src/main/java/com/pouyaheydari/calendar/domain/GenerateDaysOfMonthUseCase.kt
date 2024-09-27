package com.pouyaheydari.calendar.domain

import com.pouyaheydari.calendar.core.pojo.DayType
import com.pouyaheydari.calendar.core.pojo.GregorianDate
import com.pouyaheydari.calendar.core.pojo.ShamsiMonths
import com.pouyaheydari.calendar.core.pojo.WeekDay
import com.pouyaheydari.calendar.core.utils.CalendarTool
import com.pouyaheydari.calendar.core.utils.ResourceUtils
import javax.inject.Inject

class GenerateDaysOfMonthUseCase @Inject constructor(
    private val calculateDaysInMonthUseCase: CalculateDaysInMonthUseCase,
    private val calendarTool: CalendarTool,
    private val resourceUtils: ResourceUtils,
    private val today: DayType.Day,
) {
    operator fun invoke(iranianYear: Int, shamsiMonth: ShamsiMonths) = buildList {
        val days = calculateDaysInMonthUseCase(iranianYear, shamsiMonth)

        calendarTool.setIranianDate(iranianYear, shamsiMonth.monthNumber, day = 1)
        val firstDay = calendarTool.getIranianDate()

        val addEmptyDays = emptyDayMaker(firstDay.weekDay.distanceFromFirstDayOfWeek)
        addAll(addEmptyDays)

        for (shamsiDay in 1..days) {
            calendarTool.setIranianDate(
                year = iranianYear,
                month = shamsiMonth.monthNumber,
                day = shamsiDay
            )
            val gregorianDay = calendarTool.getGregorianDate()
            add(
                DayType.Day(
                    shamsiDay = shamsiDay,
                    shamsiMonth = shamsiMonth,
                    shamsiYear = iranianYear,
                    weekDay = gregorianDay.weekDay,
                    gregorianDay = gregorianDay.day,
                    gregorianMonth = gregorianDay.month,
                    gregorianYear = gregorianDay.year,
                    today = checkToday(gregorianDay),
                    isShamsiHoliday = checkIsHoliday(
                        iranianYear,
                        shamsiMonth,
                        shamsiDay,
                        gregorianDay.weekDay
                    )
                )
            )
        }
    }

    private fun checkIsHoliday(
        iranianYear: Int,
        shamsiMonth: ShamsiMonths,
        iranianDay: Int,
        weekDay: WeekDay
    ) = weekDay == WeekDay.Friday || (iranianYear == today.shamsiYear &&
            resourceUtils.vacationP.containsKey(shamsiMonth.monthNumber * 100 + iranianDay)
            )

    private fun checkToday(gregorianDay: GregorianDate) =
        today.gregorianYear == gregorianDay.year && today.gregorianMonth == gregorianDay.month && today.gregorianDay == gregorianDay.day

    private fun emptyDayMaker(dayOfWeek: Int) = buildList {
        repeat(dayOfWeek) {
            add(DayType.EmptyDay)
        }
    }
}
