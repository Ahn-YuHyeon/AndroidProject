package com.project.john.mygoogle.component;

import android.util.Log;

import com.project.john.mygoogle.enumeration.LogType;
public class MyLogger {
    public static void record(LogType type, String msg) {
        switch (type) {
            case VERBOSE:
                Log.v(Constant.TAG, "'" + msg + "'");
                break;
            case DEBUG:
                Log.d(Constant.TAG, "'" + msg + "'");
                break;
            case INFORMATION:
                Log.i(Constant.TAG, "'" + msg + "'");
                break;
            case WARNING:
                Log.w(Constant.TAG, "'" + msg + "'");
                break;
            case ERROR:
                Log.e(Constant.TAG, "'" + msg + "'");
                break;
        }
    }

    public static void record(LogType type, String msg, Throwable tr) {
        switch (type) {
            case VERBOSE:
                Log.v(Constant.TAG, "'" + msg + "', '" + tr + "'");
                break;
            case DEBUG:
                Log.d(Constant.TAG, "'" + msg + "', '" + tr + "'");
                break;
            case INFORMATION:
                Log.i(Constant.TAG, "'" + msg + "', '" + tr + "'");
                break;
            case WARNING:
                Log.w(Constant.TAG, "'" + msg + "', '" + tr + "'");
                break;
            case ERROR:
                Log.e(Constant.TAG, "'" + msg + "', '" + tr + "'");
                break;
        }
    }
}
