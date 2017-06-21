package com.innovation.studio.paintpic.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.innovation.studio.paintpic.data.FiltersContract.FilterEntry;
import com.innovation.studio.paintpic.data.FiltersContract.TrendingFilters;

/**
 * Filter's database class for creating db.
 */
public class FiltersDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "vision.db";
    private static final int DATABASE_VERSION = 1;

    private static final String ALTER = "ALTER TABLE ";
    private static final String RENAME_TO = " RENAME TO ";

    public FiltersDatabase(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL(FilterEntry.SQL_CREATE_TABLE);
        db.execSQL(TrendingFilters.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        db.execSQL(ALTER + FilterEntry.TABLE_NAME + RENAME_TO + FilterEntry.TABLE_NAME + "_" + DATABASE_VERSION);
        db.execSQL(ALTER + TrendingFilters.TABLE_NAME + RENAME_TO + TrendingFilters.TABLE_NAME + "_" + DATABASE_VERSION);
        onCreate(db);
    }
}
