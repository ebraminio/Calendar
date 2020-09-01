package ir.apptune.calendar.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import ir.apptune.calendar.CalendarTool;
import ir.apptune.calendar.EnglishMonthName;
import ir.apptune.calendar.MainActivity;
import ir.apptune.calendar.PersianMonthName;
import ir.apptune.calendar.PersianNumberFormatHelper;
import ir.apptune.calendar.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Shows Date Sticky Notification that contains Today's data..
 */

public class NotificationActivity extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        CalendarTool cTool = new CalendarTool();
        Intent myIntent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), myIntent, 0);
        Notification notif = new NotificationCompat.Builder(context)
                .setContentTitle(cTool.getWeekDayStr() + " " + PersianNumberFormatHelper.toPersianNumber(cTool.getIranianDay() + "")
                        + " " + PersianMonthName.getName(cTool.getIranianMonth()) + " " + PersianNumberFormatHelper.toPersianNumber(cTool.getIranianYear() + ""))
                .setContentText(PersianNumberFormatHelper.toPersianNumber(cTool.getGregorianDay() + "") + " " + EnglishMonthName.getName(cTool.getGregorianMonth()) + " " + PersianNumberFormatHelper.toPersianNumber(cTool.getGregorianYear() + ""))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .setAutoCancel(false)
                .setOngoing(true)
                .setWhen(0)
                .setPriority(1)
                .build();
        NotificationManager notificationCompatManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationCompatManager.notify(0, notif);

    }
}