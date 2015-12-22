package com.example.olesya.quickpress;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by olesya on 18-Dec-15.
 */

//The class responsible for Data Base creation and definition
public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VER = 1;
    public static final String DB_NAME = "LogTime.db";
    public DBHelper(Context context)
    {
        super(context, DB_NAME, null, DATABASE_VER);
    }

    //Table creation
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TimesContract.TimesContractEntry.TABLE_NAME + "(" + TimesContract.TimesContractEntry._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT,"+ TimesContract.TimesContractEntry.LEVEL + " INTEGER, " +
                TimesContract.TimesContractEntry.COMPLEXITY + " INTEGER, " +
                TimesContract.TimesContractEntry.BEST_RESULT + " INTEGER," +
                TimesContract.TimesContractEntry.RECENT_RESULT + " INTEGER " +
                ");");
    }

    //Table updating on upgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TimesContract.TimesContractEntry.TABLE_NAME);
        onCreate(db);
    }
    //Table updating on downgrade
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TimesContract.TimesContractEntry.TABLE_NAME);
        onCreate(db);
    }
}