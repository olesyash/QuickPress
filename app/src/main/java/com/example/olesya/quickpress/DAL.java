package com.example.olesya.quickpress;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by olesya on 18-Dec-15.
 */

//This class is for DB treatment
public class DAL {
    //Define variables
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public DAL(Context context) {
        dbHelper = new DBHelper(context);
    }

    //Function to save Best Result and Current Result to DB in suitable line for complexity and level
    public void saveTimes(int level, int complexity, long recent, long best)
    {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        //Add all values to dictionary
        values.put(TimesContract.TimesContractEntry.LEVEL, level);
        values.put(TimesContract.TimesContractEntry.COMPLEXITY, complexity);
        values.put(TimesContract.TimesContractEntry.BEST_RESULT, best);
        values.put(TimesContract.TimesContractEntry.RECENT_RESULT, recent);

        //Try to get line from DB
        long res = getRecentTime(level, complexity);
        //If no such line in DB, add it
        if(res == -1)
        {
            db.insert(TimesContract.TimesContractEntry.TABLE_NAME, null, values);
            db.close();
            Log.e("DB debug", "Inserted new");
        }
        else //If the ine exists, update it
        {
            String whereClause = TimesContract.TimesContractEntry.LEVEL + " = " + level + " AND " +
                    TimesContract.TimesContractEntry.COMPLEXITY + "=" + complexity;
            db.update(TimesContract.TimesContractEntry.TABLE_NAME, values, whereClause, null);
            db.close();
            Log.e("DB debug", "updating the existing one");
        }
    }

    //Get Best Time from DB
    public long getBestTime(int level, int complexity)
    {
        db = dbHelper.getReadableDatabase();  //get db
        Cursor cursor = db.rawQuery("SELECT "+ TimesContract.TimesContractEntry.BEST_RESULT + " FROM "+
                TimesContract.TimesContractEntry.TABLE_NAME +
                " WHERE " + TimesContract.TimesContractEntry.LEVEL + "=" + level + " AND " +
                TimesContract.TimesContractEntry.COMPLEXITY + "=" + complexity, null);
        int timeIndex = cursor.getColumnIndex(TimesContract.TimesContractEntry.BEST_RESULT);
        cursor.moveToNext();
            try {
                long br = cursor.getInt(timeIndex);
                cursor.close();
                return br;
            }
            catch (android.database.CursorIndexOutOfBoundsException e){
                cursor.close();
                return -1;
            }
    }

    //Get Resent Time from DB
    public long getRecentTime(int level, int complexity) {
        db = dbHelper.getReadableDatabase();  //get db
        Cursor cursor = db.rawQuery("SELECT " + TimesContract.TimesContractEntry.RECENT_RESULT + " FROM " +
                TimesContract.TimesContractEntry.TABLE_NAME + " WHERE " +
                TimesContract.TimesContractEntry.LEVEL + "=" + level + " AND " + TimesContract.TimesContractEntry.COMPLEXITY +
                "=" + complexity, null);
        int timeIndex = cursor.getColumnIndex(TimesContract.TimesContractEntry.RECENT_RESULT);
        cursor.moveToNext();
            try {
                long res = cursor.getInt(timeIndex);
                cursor.close();
                return res;
            } catch (android.database.CursorIndexOutOfBoundsException e) {
                cursor.close();
                return -1;
            }
    }
}

