package com.project.john.mygoogle.component;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.john.mygoogle.enumeration.DbState;
import com.project.john.mygoogle.enumeration.LogType;

public class DbHelper {
    public static SQLiteDatabase sDB;
    private DatabaseHelper mDbHelper;
    private Context mContext;
    public static DbState sDbState = DbState.NOT_FIRST;

    public DbHelper(Context context) {
        mContext = context;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                              int version) {
            super(context, name, factory, version);
            sDbState = DbState.NOT_FIRST;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(Db.CreateDb.CREATE);
            sDbState = DbState.FIRST;
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + Db.CreateDb.TABLENAME);
            onCreate(db);
        }
    }

    public DbHelper open( ) throws SQLException {
        mDbHelper = new DatabaseHelper(mContext, Constant.DB_NAME, null, Constant.DB_VERSION);
        sDB = mDbHelper.getWritableDatabase( );
        return this;
    }

    public void close( ) {
        sDB.close( );
    }

    public Cursor getAllColumns( ) {
        return sDB.query(Db.CreateDb.TABLENAME, null, null, null, null, null, null);
    }

    public boolean deleteColumn(String serial) {
        try {
            if (sDB.delete(Db.CreateDb.TABLENAME, "serial=?", new String[] {serial}) > 0) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    public boolean insertColumn(String serial, String cmd) {
        ContentValues values = new ContentValues( );

        values.put(Db.CreateDb.SERIAL, serial);
        values.put(Db.CreateDb.CMD, cmd);

        try {
            sDB.insert(Db.CreateDb.TABLENAME, null, values);
            return true;
        } catch (Exception e) {
            MyLogger.record(LogType.VERBOSE, e.toString( ), e.getCause( ));
        }
        return false;
    }

    public Cursor getColumnToSerial(String serial) throws IndexOutOfBoundsException {
        Cursor cursor =
                sDB.query(Db.CreateDb.TABLENAME, null, "serial=?", new String[] {serial}, null,
                          null, null);
        if (cursor != null && cursor.getCount( ) != 0) cursor.moveToFirst( );
        try {
            if (cursor.getString(cursor.getColumnIndex(Db.CreateDb.SERIAL)).equals(serial)) {
                return cursor;
            }
        } catch (IndexOutOfBoundsException e) {
            MyLogger.record(LogType.VERBOSE, e.toString( ), e.getCause( ));
        }
        return null;
    }
}
