package com.innovation.studio.paintpic.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.innovation.studio.paintpic.remote.VisionDBApi;

/**
 * Filters' Contract for SQLite db.
 */
public class FiltersContract {
    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.innovation.studio.paintpic";
    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FILTER = VisionDBApi.Config.PATH_FILTERS;

    public static class FilterEntry implements BaseColumns {
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FILTER;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FILTER;

        public static final String TABLE_NAME = "filter";

        // COLUMNS
        public static final String _ID = "_id";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String THUMBNAIL_URL = "thumbnail_url";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + FilterEntry.TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + TITLE + " TEXT NOT NULL,"
                + DESCRIPTION + " TEXT NOT NULL,"
                + THUMBNAIL_URL + " TEXT NOT NULL"
                + ")";

        public interface Query {
            String[] PROJECTION = {
                    TABLE_NAME + "." + FilterEntry._ID,
                    FilterEntry.TITLE,
                    FilterEntry.DESCRIPTION,
                    FilterEntry.THUMBNAIL_URL
            };
            int _ID = 0;
            int TITLE = 1;
            int DESCRIPTION = 2;
            int THUMBNAIL_URL = 3;
        }

        /**
         * Matches: /filters/
         */
        public static Uri buildDirUri() {
            return BASE_URI.buildUpon().appendPath(PATH_FILTER).build();
        }

        /**
         * Matches: /filters/[_id]/
         */
        public static Uri buildFilterUri(final long _id) {
            return BASE_URI.buildUpon().appendPath(PATH_FILTER).appendPath(Long.toString(_id)).build();
        }
    }
}
