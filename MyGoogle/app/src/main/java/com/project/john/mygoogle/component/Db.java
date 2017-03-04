package com.project.john.mygoogle.component;

import android.provider.BaseColumns;

public class Db {
    public static final class CreateDb implements BaseColumns {
        public static final String SERIAL = "serial";
        public static final String CMD = "cmd";
        public static final String TABLENAME = "cmds";
        public static final String CREATE =
                "create table " + TABLENAME + "(" + _ID + " integer primary key autoincrement, " +
                SERIAL + " text not null , " + CMD + " text not null );";
    }
}
