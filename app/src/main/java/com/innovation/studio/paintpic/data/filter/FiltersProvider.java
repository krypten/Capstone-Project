package com.innovation.studio.paintpic.data.filter;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.innovation.studio.paintpic.data.filter.FiltersContract.FilterEntry;
import com.innovation.studio.paintpic.data.filter.FiltersContract.TrendingFilters;

import static com.innovation.studio.paintpic.remote.VisionDBApi.Config.PATH_FILTERS;
import static com.innovation.studio.paintpic.remote.VisionDBApi.Config.PATH_TRENDING;

/**
 * Content Provider for providing Filters.
 *
 * @author Chaitanya Agrawal
 */
public class FiltersProvider extends ContentProvider {
    private static final String FILTER_ID_SELECTION = FilterEntry.TABLE_NAME + "." + FilterEntry._ID + " = ? ";

    private static final int FILTERS = 100;
    private static final int FILTER_ID = 101;
    private static final int FILTERS_TRENDING = 200;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FiltersContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, PATH_FILTERS, FILTERS);
        matcher.addURI(authority, PATH_FILTERS + "/#", FILTER_ID);
        matcher.addURI(authority, PATH_FILTERS + "/" + PATH_TRENDING, FILTERS_TRENDING);
        return matcher;
    }

    private SQLiteOpenHelper mDBHelper;

    @Override
    public boolean onCreate() {
        mDBHelper = new FiltersDatabase(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
        final SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case FILTERS:
                retCursor = db.query(FilterEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FILTER_ID:
                retCursor = getFilterById(db, uri, projection, sortOrder);
                break;
            case FILTERS_TRENDING:
                retCursor = getFiltersByReference(db, TrendingFilters.TABLE_NAME, projection, selection, selectionArgs, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Query for unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(final Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case FILTERS:
                return FilterEntry.CONTENT_TYPE;
            case FILTER_ID:
                return FilterEntry.CONTENT_ITEM_TYPE;
            case FILTERS_TRENDING:
                return TrendingFilters.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(final Uri uri, final ContentValues values) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case FILTERS: {
                long _id = db.insertWithOnConflict(FilterEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                if (_id > 0)
                    returnUri = FilterEntry.buildFilterUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case FILTER_ID: {
                long _id = db.insertWithOnConflict(FilterEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                if (_id > 0)
                    returnUri = FilterEntry.buildFilterUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case FILTERS_TRENDING: {
                long _id = db.insertWithOnConflict(TrendingFilters.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                if (_id > 0)
                    returnUri = TrendingFilters.buildTrendingFiltersUri();
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int bulkInsert(@NonNull final Uri uri, @NonNull final ContentValues[] values) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int insertCount = 0;
        switch (sUriMatcher.match(uri)) {
            case FILTERS:
                db.beginTransaction();
                try {
                    for (final ContentValues value : values) {
                        final long id = db.insertWithOnConflict(FilterEntry.TABLE_NAME, null, value, SQLiteDatabase.CONFLICT_REPLACE);
                        if (id != -1) {
                            insertCount++;
                        }
                    }
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return insertCount;
            default:
                insertCount = super.bulkInsert(uri, values);
        }
        return insertCount;
    }

    @Override
    public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
        return 0;
    }

    @Override
    public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int rowsDeleted;
        switch (sUriMatcher.match(uri)) {
            case FILTERS:
                rowsDeleted = db.delete(FilterEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FILTER_ID:
                final long id = FilterEntry.getFilterId(uri);
                rowsDeleted = db.delete(FilterEntry.TABLE_NAME,
                        FILTER_ID_SELECTION, new String[]{Long.toString(id)});
                break;
            case FILTERS_TRENDING:
                rowsDeleted = db.delete(TrendingFilters.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public void shutdown() {
        mDBHelper.close();
        super.shutdown();
    }

    /* QUERY UTILITY METHODS */

    private Cursor getFilterById(final SQLiteDatabase db, final Uri uri, final String[] projection, final String sortOrder) {
        final String[] SLECTION_ARGS = new String[]{Long.toString(FilterEntry.getFilterId(uri))};
        return db.query(
                FilterEntry.TABLE_NAME,
                projection,
                FILTER_ID_SELECTION,
                SLECTION_ARGS,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getFiltersByReference(final SQLiteDatabase db,
                                         final String tableName,
                                         final String[] projection,
                                         final String selection,
                                         final String[] selectionArgs,
                                         final String sortOrder) {
        final SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        // tableName INNER JOIN filters ON tableName.filter_id = filter._id
        sqLiteQueryBuilder.setTables(tableName + " INNER JOIN " + FilterEntry.TABLE_NAME
                + " ON " + tableName + "." + FiltersContract.COLUMN_FILTER_ID_KEY
                + " = " + FilterEntry.TABLE_NAME + "." + FilterEntry._ID);
        return sqLiteQueryBuilder.query(
                db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }
}
