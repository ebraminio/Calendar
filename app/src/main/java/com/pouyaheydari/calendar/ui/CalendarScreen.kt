package com.pouyaheydari.calendar.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.view.View
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.material.snackbar.Snackbar
import com.pouyaheydari.calendar.R
import com.pouyaheydari.calendar.core.pojo.DayType
import com.pouyaheydari.calendar.core.utils.toPersianNumber
import com.pouyaheydari.calendar.domain.Month
import com.pouyaheydari.calendar.ui.components.DayDetailsBottomSheet
import com.pouyaheydari.calendar.ui.components.EventComponent
import com.pouyaheydari.calendar.ui.components.HeaderComponent
import com.pouyaheydari.calendar.ui.components.MonthComponent
import com.pouyaheydari.calendar.ui.components.MonthYearTitleComponent
import com.pouyaheydari.calendar.ui.components.WeekDaysComponent
import java.util.GregorianCalendar

// We have to add this to prevent misplacement of dates when combining persian text with numbers
internal const val RLM = '\u200F'
private const val CALENDAR_INTENT_TYPE = "vnd.android.cursor.item/event"

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    viewModel: CalendarViewModel = viewModel(),
) {
    val state = viewModel.screenState.collectAsStateWithLifecycle().value
    val context = LocalContext.current
    val view = LocalView.current

    CalendarComponent(
        modifier = modifier,
        paddingValues = paddingValues,
        today = state.today,
        month = state.displayMonth,
        context = context,
        onDaySelected = { viewModel.onIntent(CalendarUserIntents.OnDayClicked(it)) },
        onNextMonthClicked = { viewModel.onIntent(CalendarUserIntents.OnNextMonthClicked) },
        onPreviousMonthClicked = { viewModel.onIntent(CalendarUserIntents.OnPreviousMonthClicked) })

    DayDetailsBottomSheet(
        shouldShowBottomSheet = state.shouldShowBottomSheet,
        onBottomSheetDismissed = { viewModel.onIntent(CalendarUserIntents.OnBottomSheetDismissed) },
        setReminderText = stringResource(R.string.set_a_work_to_do),
        onSetReminderClicked = { handleIntentToDefaultCalendarApp(state, context, view) },
        isHoliday = state.selectedDay.isShamsiHoliday,
        selectedDay = state.selectedDay
    )
}

private fun handleIntentToDefaultCalendarApp(
    state: CalendarScreenState,
    context: Context,
    view: View
) {
    val intent = Intent(Intent.ACTION_EDIT).apply {
        type = CALENDAR_INTENT_TYPE
        putExtra(
            CalendarContract.EXTRA_EVENT_BEGIN_TIME,
            GregorianCalendar(
                state.selectedDay.gregorianYear,
                state.selectedDay.gregorianMonth.monthNumber - 1,
                state.selectedDay.gregorianDay
            ).timeInMillis
        )
    }
    try {
        context.startActivity(intent, null)
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
        Snackbar.make(
            view,
            getString(context, R.string.google_calendar_is_not_installed),
            Snackbar.LENGTH_LONG
        ).show()
    }
}

@Composable
private fun CalendarComponent(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    today: DayType.Day,
    month: Month,
    context: Context,
    onDaySelected: (DayType.Day) -> Unit,
    onNextMonthClicked: () -> Unit = {},
    onPreviousMonthClicked: () -> Unit = {}
) {
    Column(
        modifier = modifier.padding(bottom = paddingValues.calculateBottomPadding())
    ) {
        HeaderComponent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
            dayOfWeek = stringResource(
                id = R.string.today_is_day_of_week,
                today.weekDay.getName(context)
            ),
            iranianDate = RLM + stringResource(
                id = R.string.persian_day_month,
                today.shamsiDay.toPersianNumber(),
                today.shamsiMonth.getName(context),
                today.shamsiYear.toPersianNumber()
            ),
            gregorianDate = RLM + stringResource(
                id = R.string.gregorian_full_date,
                today.gregorianDay.toPersianNumber(),
                today.gregorianMonth.getName(context),
                today.gregorianYear.toPersianNumber()
            )
        )
        Spacer(modifier = Modifier.padding(all = 8.dp))
        MonthYearTitleComponent(
            shamsiDate = stringResource(
                R.string.month_year,
                month.shamsiYear.toPersianNumber(),
                month.shamsiMonth.getName(context)
            ),
            gregorianDate = generateDisplayedGregorianTitle(month, context),
            onNextMonthClicked = onNextMonthClicked,
            onPreviousMonthClicked = onPreviousMonthClicked
        )
        Spacer(modifier = Modifier.padding(all = 8.dp))
        WeekDaysComponent(weekDays = getWeekDays())
        AnimatedContent(month.days, label = "DaysAnimatedContent") {
            MonthComponent(
                list = it,
                onItemClicked = onDaySelected,
                onSwipeToNextMonth = onNextMonthClicked,
                onSwipeToPreviousMonth = onPreviousMonthClicked
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp, top = 16.dp),
            text = stringResource(R.string.events, month.shamsiMonth.getName(context)),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End
        )
        AnimatedContent(month.days, label = "EventsAnimatedContent") {
            LazyColumn(
                modifier = Modifier.animateContentSize(),
                contentPadding = PaddingValues(top = 8.dp)
            ) {
                items(it) { day ->
                    if (day is DayType.Day && day.events.isNotEmpty()) {
                        EventComponent(
                            modifier = Modifier.clickable { onDaySelected(day) },
                            day = day
                        )
                    }
                }
            }
        }
    }
}

private fun generateDisplayedGregorianTitle(month: Month, context: Context) = buildString {
    month.gregorianMonths.forEach {
        append(it.getName(context))
        if (it != month.gregorianMonths.last()) {
            append(" - ")
        } else {
            append("  ")
        }
    }
    month.gregorianYear.forEach {
        append(it.toPersianNumber())
        if (it != month.gregorianYear.last()) {
            append(" - ")
        }
    }
}

@Composable
private fun getWeekDays() = listOf(
    stringResource(id = R.string.saturday),
    stringResource(id = R.string.sunday),
    stringResource(id = R.string.monday),
    stringResource(id = R.string.tuesday),
    stringResource(id = R.string.wednesdays),
    stringResource(id = R.string.thursday),
    stringResource(id = R.string.friday),
).reversed()