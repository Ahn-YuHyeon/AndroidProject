package com.project.john.bef.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.john.bef.component.Constant;
import com.project.john.bef.component.DbItem;
import com.project.john.bef.component.DuplicateException;
import com.project.john.bef.component.InputException;
import com.project.john.bef.enumeration.LogType;

public class DbHelper {
    public static SQLiteDatabase sDB;
    private DatabaseHelper mDBHelper;
    private Context mContext;

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                              int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DbItem.CreateDb.CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DbItem.CreateDb.TABLENAME);
            onCreate(db);
        }
    }

    public DbHelper(Context context) {
        mContext = context;
    }

    public DbHelper open( ) throws SQLException {
        mDBHelper = new DatabaseHelper(mContext, Constant.DB_NAME, null, Constant.DB_VERSION);
        sDB = mDBHelper.getWritableDatabase( );
        return this;
    }

    public void close( ) {
        sDB.close( );
    }

    public Cursor getAllColumns( ) {
        return sDB.query(DbItem.CreateDb.TABLENAME, null, null, null, null, null, null);
    }

    public Cursor getColumn(long id) {
        Cursor cursor =
                sDB.query(DbItem.CreateDb.TABLENAME, null, "_id=" + id, null, null, null, null);
        if (cursor != null && cursor.getCount( ) != 0) cursor.moveToFirst( );
        return cursor;
    }

    public boolean deleteColumn(long id) {
        return sDB.delete(DbItem.CreateDb.TABLENAME, "_id=" + id, null) > 0;
    }

    public Cursor getColumnToLogin(String email) throws IndexOutOfBoundsException {
        Cursor cursor =
                sDB.query(DbItem.CreateDb.TABLENAME, null, "email=?", new String[] {email}, null,
                          null, null);
        if (cursor != null && cursor.getCount( ) != 0) cursor.moveToFirst( );
        try {
            if (cursor.getString(cursor.getColumnIndex("email")).equals(email)) {
                return cursor;
            }
        } catch (IndexOutOfBoundsException e) {
            Logger.record(LogType.VERBOSE, email + Constant.EMAIL_ERROR_MSG);
        }
        return null;
    }

    public Cursor getColumnToMember(String email) throws IndexOutOfBoundsException {
        Cursor cursor =
                sDB.query(DbItem.CreateDb.TABLENAME, null, "email=?", new String[] {email}, null,
                          null, null);
        if (cursor != null && cursor.getCount( ) != 0) cursor.moveToFirst( );
        try {
            if (cursor.getString(cursor.getColumnIndex("email")).equals(email)) {
                return null;
            }
        } catch (IndexOutOfBoundsException e) {
            Logger.record(LogType.VERBOSE, email + Constant.JOINED_MB_MSG);
        }
        return cursor;
    }

    public Cursor getMatchEmail(String email) {
        try {
            Cursor cursor =
                    sDB.rawQuery("select * from membership where email=" + "'" + email + "'", null);
            return cursor;
        } catch (Exception e) {
            Logger.record(LogType.VERBOSE, e.toString( ), e.getCause( ));
        }
        return null;
    }

    public boolean deleteColumn(String email) {
        try {
            if (sDB.delete(DbItem.CreateDb.TABLENAME, "email=?", new String[] {email}) > 0) {
                return true;
            }
        } catch (Exception e) {
            Logger.record(LogType.VERBOSE, e.toString( ), e.getCause( ));
        }
        return false;
    }

    public boolean insertColumn(String[] strings, String sex)
            throws InputException, DuplicateException {
        ContentValues values = new ContentValues( );

        if ((strings == null) || (sex == null)) {
            throw new InputException(Constant.BLANK_ERROR_MSG);
        } else {
            if (getColumnToMember(strings[0]) == null) {
                throw new DuplicateException(Constant.DUPLICATE_ERROR_MSG);
            }
        }
        values.put(DbItem.CreateDb.EMAIL, strings[0]);
        values.put(DbItem.CreateDb.PASSWORD, strings[1]);
        values.put(DbItem.CreateDb.NAME, strings[2]);
        values.put(DbItem.CreateDb.BIRTH, strings[3]);
        values.put(DbItem.CreateDb.CITY, strings[4]);
        values.put(DbItem.CreateDb.JOB, strings[5]);
        values.put(DbItem.CreateDb.SEX, sex);

        try {
            sDB.insert(DbItem.CreateDb.TABLENAME, null, values);
            return true;
        } catch (Exception e) {
            Logger.record(LogType.VERBOSE, e.toString( ), e.getCause( ));
        }
        return false;
    }

    public boolean updateColumn(int id, String[] strings, String sex) {
        ContentValues values = new ContentValues( );

        values.put(DbItem.CreateDb.EMAIL, strings[0]);
        values.put(DbItem.CreateDb.PASSWORD, strings[1]);
        values.put(DbItem.CreateDb.NAME, strings[2]);
        values.put(DbItem.CreateDb.BIRTH, strings[3]);
        values.put(DbItem.CreateDb.CITY, strings[4]);
        values.put(DbItem.CreateDb.JOB, strings[5]);
        values.put(DbItem.CreateDb.SEX, sex);
        return sDB.update(DbItem.CreateDb.TABLENAME, values, "_id=" + id, null) > 0;
    }
}
