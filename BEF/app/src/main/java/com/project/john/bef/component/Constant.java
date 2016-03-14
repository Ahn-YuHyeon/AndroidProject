package com.project.john.bef.component;

import java.util.Locale;

public class Constant {
    /**
     * INTERFACE
     */
    public static final int BUTTON_CNT = 9;
    public static final int GOOGLE_STT = 1000;
    public static final int PITCH = 10;
    public static final int RATE = 8;
    public static final Locale LANGUAGE = Locale.KOREA;
    public static final String POSITIVE_RESPONSE = "그래";
    public static final String NEGATIVE_RESPONSE = "아니";
    public static final String OK = "OK";
    public static final String CANCEL = "CANCEL";
    public static final String MALE = "MALE";
    public static final String FEMALE = "FEMALE";
    public static final String TAG = "JONATHAN";
    /**
     * DATABASE
     */
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "membership.db";
    /**
     * INFORMATION
     */
    public static final String FINISHED_APP_MSG =
            "If you want to finish app, please press back key again.";
    public static final String OPENED_DB_MSG = "It succeeded to open the database.";
    public static final String CLOSED_DB_MSG = "It succeeded to close the database.";
    public static final String JOINED_MB_MSG = " succeeded to become a member.";
    public static final String INPUT_GUIDE_MSG = "Input the text of guide voice.";
    public static final String INPUT_RUN_MSG = "Input the text of run voice.";
    public static final String STARTED_RUN_MSG = "Start to Playing BEF.";
    public static final String SUCCESSED_INIT_MSG = "Successed to init.";
    public static final String FAILED_INIT_MSG = "Failed to init.";
    public static final String SUCCESSED_RECO_MSG = "Successed recognition.";
    public static final String SUCCESSED_ALARM_MSG = "Happened alarm.";
    public static final String INPUT_VOICE_MSG = "\"그래\" 또는 \"아니\"라고 말하세요.";
    /**
     * EXCEPTION
     */
    public static final String BLANK_ERROR_MSG = "It failed to confirm whether blank existed.";
    public static final String DUPLICATE_ERROR_MSG =
            "It failed to confirm whether email is duplicated.";
    public static final String DELETE_ERROR_MSG = "It failed to delete values.";
    public static final String INSERT_ERROR_MSG = "It failed to insert values.";
    public static final String LOGIN_ERROR_MSG = "It failed to confirm your state of login.";
    public static final String EMAIL_ERROR_MSG = "It failed to confirm your email.";
    public static final String PW_ERROR_MSG = "It failed to match your email and password.";
    public static final String SHAKE_ERROR_MSG = "It detected to shake device within 3seconds.";
}
