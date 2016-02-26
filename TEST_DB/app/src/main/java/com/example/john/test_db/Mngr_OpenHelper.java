package com.example.john.test_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Mngr_OpenHelper {
    private static final String DATABASE_NAME = "addressbook.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;

    private class DatabaseHelper extends SQLiteOpenHelper {

        // 생성자
        public DatabaseHelper(Context context, String name,
                              CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // 최초 DB를 만들때 한번만 호출된다.
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(Cpnt_DataBase.CreateDB._CREATE);

        }

        // 버전이 업데이트 되었을 경우 DB를 다시 만들어 준다.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+Cpnt_DataBase.CreateDB._TABLENAME);
            onCreate(db);
        }
    }

    public Mngr_OpenHelper(Context context){
        this.mCtx = context;
    }

    public Mngr_OpenHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        mDB.close( );
    }

    // Insert DB
    public long insertColumn(String name, String contact, String email){
        ContentValues values = new ContentValues();
        values.put(Cpnt_DataBase.CreateDB.NAME, name);
        values.put(Cpnt_DataBase.CreateDB.CONTACT, contact);
        values.put(Cpnt_DataBase.CreateDB.EMAIL, email);
        return mDB.insert(Cpnt_DataBase.CreateDB._TABLENAME, null, values);
    }

    // Update DB
    public boolean updateColumn(long id , String name, String contact, String email){
        ContentValues values = new ContentValues();
        values.put(Cpnt_DataBase.CreateDB.NAME, name);
        values.put(Cpnt_DataBase.CreateDB.CONTACT, contact);
        values.put(Cpnt_DataBase.CreateDB.EMAIL, email);
        return mDB.update(Cpnt_DataBase.CreateDB._TABLENAME, values, "_id=" + id, null) > 0;
    }

    // Delete ID
    public boolean deleteColumn(long id){
        return mDB.delete(Cpnt_DataBase.CreateDB._TABLENAME, "_id="+id, null) > 0;
    }

    // Delete Contact
    public boolean deleteColumn(String number){
        try {
            return mDB.delete(Cpnt_DataBase.CreateDB._TABLENAME, "contact=?", new String[]{number})>0;
        }catch (Exception e){
            e.printStackTrace( );
            return false;
        }
    }

    // Select All
    public Cursor getAllColumns(){
        return mDB.query(Cpnt_DataBase.CreateDB._TABLENAME, null, null, null, null, null, null);
    }

    // ID 컬럼 얻어 오기
    public Cursor getColumn(long id){
        Cursor c = mDB.query(Cpnt_DataBase.CreateDB._TABLENAME, null,
                             "_id="+id, null, null, null, null);
        if(c != null && c.getCount() != 0)
            c.moveToFirst();
        return c;
    }

    // 이름 검색 하기 (rawQuery)
    public Cursor getMatchName(String name){
        Cursor c = mDB.rawQuery( "select * from address where name=" + "'" + name + "'" , null);
        return c;
    }
}
