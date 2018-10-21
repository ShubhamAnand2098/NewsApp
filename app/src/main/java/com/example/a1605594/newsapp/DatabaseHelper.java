package com.example.a1605594.newsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Newsapp.db";
    private static final String TABLE_NAME = "USER_DETAILS_TABLE";
    private static final String COLUMN1 = "ID";
    private static final String COLUMN2 = "NAME";
    private static final String COLUMN3 = "EMAIL_ID";
    private static final String COLUMN4 = "PASSWORD";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME+"("+COLUMN1+" INTEGER PRIMARY KEY AUTOINCREMENT, "
        +COLUMN2+" TEXT, "+COLUMN3+" TEXT, "+COLUMN4+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
    }

    public boolean insert_data(String name, String email, String password)
    {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor s = sqLiteDatabase.rawQuery("SELECT "+COLUMN1+" FROM " + TABLE_NAME + " WHERE " + COLUMN3 + " = ? ", new String[]{email});
        if(s.getCount() == 0) {
            s.close();
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN2, name);
            contentValues.put(COLUMN3, email);
            contentValues.put(COLUMN4, password);
            long result = database.insert(TABLE_NAME, null, contentValues);
            return result != -1;
        }
        else {
            s.close();
            return false;
        }
    }

    public boolean if_exists(String email, String password)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN3 + " = ? AND " +
                COLUMN4 + " = ? ", new String[]{email, password});

        if(cursor.getCount() == 0)
        {
            cursor.close();
            return false;
        }
        else {
            cursor.close();
            return true;
        }


    }

    public String getName(String email)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + COLUMN2 + " FROM " + TABLE_NAME + " WHERE "
        + COLUMN3 + " = ?", new String[]{email});

        String n = "GUEST";
        if(cursor.moveToFirst())
            n = cursor.getString(cursor.getColumnIndex(COLUMN2));

        return n;
    }


}
