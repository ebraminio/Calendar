package com.pouyaheydari.calendar.widget.components

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle

@Composable
internal fun CalendarWidgetComponent(
    persianDate: String = "",
    gregorianDate: String = "",
    mainActivity: Class<out AppCompatActivity>? = null
) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .clickable {
                mainActivity?.let { actionStartActivity(it) }
            }
            .padding(16.dp)
            .background(GlanceTheme.colors.widgetBackground),
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally
    ) {
        Text(
            modifier = GlanceModifier.defaultWeight(),
            maxLines = 1,
            text = persianDate,
            style = TextStyle(
                color = GlanceTheme.colors.onSurface,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            ),
        )
        Text(
            modifier = GlanceModifier.defaultWeight(),
            maxLines = 1,
            text = gregorianDate,
            style = TextStyle(
                color = GlanceTheme.colors.onSurface,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
            ),
        )
    }
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview
@Composable
private fun Preview() {
    CalendarWidgetComponent(
        persianDate = "شنبه ۲۸ اردیبهشت ۱۴۰۳",
        gregorianDate = "۲۳ ژوئیه ۲۰۲۴",
        mainActivity = null
    )
}
