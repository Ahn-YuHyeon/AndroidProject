package com.project.john.bef.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.project.john.bef.component.OptionItem;
import com.project.john.bef.enumeration.LogType;

public class AlarmReceiver extends BroadcastReceiver {
    private static OnAlarmListener mOnAlarmListener;

    public interface OnAlarmListener {
        void onAlarm(OptionItem opItem);
    }

    public void setOnAlarmListener(OnAlarmListener listener) {
        mOnAlarmListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            OptionItem opItem = (OptionItem)intent.getSerializableExtra("data");
            mOnAlarmListener.onAlarm(opItem);
        } catch (Exception e)
        {
            Logger.record(LogType.VERBOSE, e.toString( ) + e.getCause( ));
        }
    }
}
