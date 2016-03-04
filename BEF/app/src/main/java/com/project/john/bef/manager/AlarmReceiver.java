package com.project.john.bef.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    private static OnAlarmListener mOnAlarmListener;

    public interface OnAlarmListener {
        void onAlarm(int index);
    }

    public void setOnAlarmListener(OnAlarmListener listener) {
        mOnAlarmListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int index = 0;
        index = intent.getExtras( ).getInt("Index");
        mOnAlarmListener.onAlarm(index);
    }
}
