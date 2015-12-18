package com.example.olesya.quickpress;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.olesya.quickpress.TimesContract;
/**
 * Created by olesya on 18-Dec-15.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VER = 2;
    public static final String DB_NAME = "LogTime.db";
    public DBHelper(Context context)
    {
        super(context, DB_NAME, null, DATABASE_VER);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TimesContract.TimesContractEntry.TABLE_NAME + "(" + TimesContract.TimesContractEntry._ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT,"+ TimesContract.TimesContractEntry.LEVEL + " INTEGER UNIQUE, " +
                TimesContract.TimesContractEntry.COMPLEXITY + " INTEGER UNIQUE, " +
                TimesContract.TimesContractEntry.BEST_RESULT + " REAL," +
                TimesContract.TimesContractEntry.RECENT_RESULT + " REAL " +
                ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + TimesContract.TimesContractEntry.TABLE_NAME);
        onCreate(db);
    }


}