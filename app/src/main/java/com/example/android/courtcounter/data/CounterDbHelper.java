package com.example.android.courtcounter.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.courtcounter.data.CounterContract.CounterEntry;

/**
 * Created by OKHAMAFE EMMANUEL on 5/8/2018.
 */

public class CounterDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = CounterDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "counter.db";

    private static final int DATABASE_VERSION = 1;

    public CounterDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //todo use date for db column type not int
        String SQL_CREATE_COUNTERS_TABLE = "CREATE TABLE " + CounterEntry.TABLE_NAME + " ("
                + CounterEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CounterEntry.COLUMN_TEAM_A_SCORE + " INTEGER NOT NULL, "
                + CounterEntry.COLUMN_TEAM_B_SCORE + " INTEGER NOT NULL, "
                + CounterEntry.COLUMN_DATE + " INTEGER NOT NULL);";

        db.execSQL(SQL_CREATE_COUNTERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
