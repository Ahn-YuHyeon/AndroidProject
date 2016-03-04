package com.project.john.bef.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.project.john.bef.activity.Option;
import com.project.john.bef.component.OptionItem;
import com.project.john.bef.enumeration.LogType;

import java.util.Calendar;

public class AlarmRegister {
    static int[] weakUpTimeH = new int[9];
    static int[] weakUpTimeM = new int[9];

    public void setAlarm(Context context) {
        AlarmManager alarmMngr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pIntent;

        Calendar calendar = Calendar.getInstance( );

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        int i = 0;
        for (OptionItem opItem : Option.sOpItems) {
            weakUpTimeH[i] = opItem.mOprHour;
            weakUpTimeM[i] = opItem.mOprMinute;

            Logger.record(LogType.VERBOSE, "Alarm" + (i + 1) + "(" + weakUpTimeH[i] + ":" +
                                           weakUpTimeM[i] + ")");

            if (weakUpTimeH[i] > 0 && weakUpTimeM[i] > 0) {
                if (((hour * 60) + minute) > ((weakUpTimeH[i] * 60) + weakUpTimeM[i])) {
                    intent.putExtra("index", i);
                    pIntent = PendingIntent.getBroadcast(context, i, intent, 0);
                    calendar.set(year, month, day + 1, weakUpTimeH[i], weakUpTimeM[i]);
                    alarmMngr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis( ),
                                           24 * 60 * 60 * 1000, pIntent);
                } else {
                    intent.putExtra("index", i);
                    pIntent = PendingIntent.getBroadcast(context, i, intent, 0);
                    calendar.set(year, month, day, weakUpTimeH[i], weakUpTimeM[i]);
                    alarmMngr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis( ),
                                           24 * 60 * 60 * 1000, pIntent);
                }
            }
            i++;
        }
    }

    public void releaseAlarm(Context context) {
        AlarmManager alarmMngr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent( );

        int i = 0;
        for (OptionItem opItem : Option.sOpItems) {
            PendingIntent pIntent = PendingIntent.getActivity(context, i, intent, 0);
            alarmMngr.cancel(pIntent);
            i++;
        }
    }
}
