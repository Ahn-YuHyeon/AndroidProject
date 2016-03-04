package com.project.john.bef.component;

import android.provider.BaseColumns;

public final class DbItem {
    public static final class CreateDb implements BaseColumns {
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
        public static final String NAME = "name";
        public static final String BIRTH = "birth";
        public static final String CITY = "city";
        public static final String JOB = "job";
        public static final String SEX = "sex";
        public static final String TABLENAME = "membership";
        public static final String CREATE =
                "create table " + TABLENAME + "(" + _ID + " integer primary key autoincrement, " +
                EMAIL + " text not null , " + PASSWORD + " text not null , " + NAME +
                " text not null , " + BIRTH + " text not null , " + CITY + " text not null , " +
                JOB + " text not null , " + SEX + " text not null );";
    }
}