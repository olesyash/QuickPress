package com.example.olesya.quickpress;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by olesya on 18-Dec-15.
 */
public class DAL {

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public DAL(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void saveTimes(int level, int complexity, long recent, long best)
    {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TimesContract.TimesContractEntry.LEVEL, level);
        values.put(TimesContract.TimesContractEntry.COMPLEXITY, complexity);
        values.put(TimesContract.TimesContractEntry.BEST_RESULT, best);
        values.put(TimesContract.TimesContractEntry.RECENT_RESULT, recent);
        db.insert(TimesContract.TimesContractEntry.TABLE_NAME, null, values);
        db.close();

    }
    public Cursor getCursor()
    {
        db = dbHelper.getReadableDatabase();  //get db
        return db.rawQuery("SELECT * FROM " + TimesContract.TimesContractEntry.TABLE_NAME, null); //get data
    }

    public long getBestTime(int level, int complexity)
    {
        db = dbHelper.getReadableDatabase();  //get db
        Cursor cursor = db.rawQuery("SELECT "+ TimesContract.TimesContractEntry.BEST_RESULT + " FROM "+ TimesContract.TimesContractEntry.TABLE_NAME + "WHERE " +
                TimesContract.TimesContractEntry.LEVEL + "=" + level + "AND" + TimesContract.TimesContractEntry.COMPLEXITY +
                "=" + complexity, null);
        int timeIndex = cursor.getColumnIndex(TimesContract.TimesContractEntry.BEST_RESULT);
        return cursor.getLong(timeIndex);
    }

    public long getRecentTime(int level, int complexity)
    {
        db = dbHelper.getReadableDatabase();  //get db
        Cursor cursor = db.rawQuery("SELECT "+ TimesContract.TimesContractEntry.RECENT_RESULT + " FROM " +
                TimesContract.TimesContractEntry.TABLE_NAME + "WHERE " +
                TimesContract.TimesContractEntry.LEVEL + "=" + level + "AND" + TimesContract.TimesContractEntry.COMPLEXITY +
                "=" + complexity, null);
        int timeIndex = cursor.getColumnIndex(TimesContract.TimesContractEntry.RECENT_RESULT);
        return cursor.getLong(timeIndex);
    }
}