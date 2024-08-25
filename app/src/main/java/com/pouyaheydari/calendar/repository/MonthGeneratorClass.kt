package com.pouyaheydari.calendar.repository

import com.pouyaheydari.calendar.core.pojo.CalendarModel
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
    private val currentDate: CalendarModel
) {

    fun getMonthList(monthType: MonthType): List<CalendarModel> {
        val list = arrayListOf<CalendarModel>()
        val month =
            if (monthType == MonthType.NEXT_MONTH) getNextMonthDate() else getPreviousMonthDate()
        list.addAll(addEmptyDays(calendar.dayOfWeek))
        for (i in 1..month.dayNumber) {
            with(calendar) {
                setIranianDate(month.year, month.month, i)
                list.add(
                    CalendarModel(
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

    private fun checkIsToday(calendarModel: CalendarModel) {
        calendarModel.today =
            calendarModel.iranianDay != -1 &&
                    currentDate.iranianYear == calendarModel.iranianYear &&
                    currentDate.iranianMonth == calendarModel.iranianMonth &&
                    currentDate.iranianDay == calendarModel.iranianDay
    }

    private fun checkIsHoliday(calendarModel: CalendarModel) = with(calendarModel) {
        if (dayOfWeek == FRIDAY || (iranianYear == currentDate.iranianYear && ResourceUtils.vacationP.containsKey(
                iranianMonth * 100 + iranianDay
            ))
        )
            isHoliday = true
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

    private fun addEmptyDays(dayOfWeek: Int): ArrayList<CalendarModel> = when (dayOfWeek) {
        MONDAY -> emptyDayMaker(2)
        TUESDAY -> emptyDayMaker(3)
        WEDNESDAY -> emptyDayMaker(4)
        THURSDAY -> emptyDayMaker(5)
        FRIDAY -> emptyDayMaker(6)
        SATURDAY -> emptyDayMaker(0)
        SUNDAY -> emptyDayMaker(1)
        else -> throw IllegalArgumentException("Day is not defined in the week!")
    }

    private fun emptyDayMaker(dayOfWeek: Int): ArrayList<CalendarModel> {
        val list = arrayListOf<CalendarModel>()
        for (i in 1..dayOfWeek) {
            list.add(
                CalendarModel(
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