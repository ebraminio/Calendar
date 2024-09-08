package com.pouyaheydari.calendar.repository

import com.pouyaheydari.calendar.core.pojo.Day
import com.pouyaheydari.calendar.core.utils.CalendarTool
import com.pouyaheydari.calendar.core.utils.EMPTY_DATE
import com.pouyaheydari.calendar.core.utils.ResourceUtils
import com.pouyaheydari.calendar.pojo.DateModel
import com.pouyaheydari.calendar.pojo.MonthType
import javax.inject.Inject

private const val MONDAY = 0
private const val TUESDAY = 1
private const val WEDNESDAY = 2
private const val THURSDAY = 3
private const val FRIDAY = 4
private const val SATURDAY = 5
private const val SUNDAY = 6

/**
 * This class generated a month before or after the current month that the user is looking at
 *
 * @property calendar: The object that holds the current date that the user is looking
 */
class MonthGeneratorClass @Inject constructor(
    private var calendar: CalendarTool,
    private val currentDate: Day
) {

    fun getMonthList(monthType: MonthType): List<Day> {
        val list = arrayListOf<Day>()
        val month =
            if (monthType == MonthType.NEXT_MONTH) getNextMonthDate() else getPreviousMonthDate()
        list.addAll(addEmptyDays(calendar.dayOfWeek))
        for (i in 1..month.dayNumber) {
            with(calendar) {
                setIranianDate(month.year, month.month, i)
                list.add(
                    Day(
                        iranianDay,
                        iranianMonth,
                        iranianYear,
                        dayOfWeek,
                        gregorianDay,
                        gregorianMonth,
                        gregorianYear
                    ).apply {
                        checkIsHoliday(this)
                        checkIsToday(this)
                    })
            }
        }
        return list
    }

    private fun checkIsToday(day: Day) {
        day.today =
            day.shamsiDay != -1 &&
                    currentDate.shamsiYear == day.shamsiYear &&
                    currentDate.shamsiMonth == day.shamsiMonth &&
                    currentDate.shamsiDay == day.shamsiDay
    }

    private fun checkIsHoliday(day: Day) = with(day) {
        if (dayOfWeek == FRIDAY || (shamsiYear == currentDate.shamsiYear && ResourceUtils.vacationP.containsKey(
                shamsiMonth * 100 + shamsiDay
            ))
        )
            isShamsiHoliday = true
    }

    private fun getNextMonthDate(): DateModel {
        var month = calendar.iranianMonth
        var year = calendar.iranianYear

        if (month + 1 <= 12) {
            month++
        } else {
            month = 1
            year++
        }

        calendar.setIranianDate(year, month, 1)
        val dayNumber =
            if (month <= 6) 31 else if (month == 12 && !calendar.isLeap(calendar.iranianYear)) 29 else 30
        return DateModel(year, month, dayNumber)
    }

    private fun getPreviousMonthDate(): DateModel {
        var month = calendar.iranianMonth
        var year = calendar.iranianYear

        if (month - 1 > 1)
            month--
        else {
            month = 12
            year--
        }

        calendar.setIranianDate(year, month, 1)
        val dayNumber =
            if (month <= 6) 31 else if (month == 12 && !calendar.isLeap(calendar.iranianYear)) 29 else 30
        return DateModel(year, month, dayNumber)
    }

    private fun addEmptyDays(dayOfWeek: Int): ArrayList<Day> = when (dayOfWeek) {
        MONDAY -> emptyDayMaker(2)
        TUESDAY -> emptyDayMaker(3)
        WEDNESDAY -> emptyDayMaker(4)
        THURSDAY -> emptyDayMaker(5)
        FRIDAY -> emptyDayMaker(6)
        SATURDAY -> emptyDayMaker(0)
        SUNDAY -> emptyDayMaker(1)
        else -> throw IllegalArgumentException("Day is not defined in the week!")
    }

    private fun emptyDayMaker(dayOfWeek: Int): ArrayList<Day> {
        val list = arrayListOf<Day>()
        for (i in 1..dayOfWeek) {
            list.add(
                Day(
                    EMPTY_DATE,
                    EMPTY_DATE,
                    EMPTY_DATE,
                    EMPTY_DATE,
                    EMPTY_DATE,
                    EMPTY_DATE,
                    EMPTY_DATE
                )
            )
        }
        return list
    }
}