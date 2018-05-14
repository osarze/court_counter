package com.example.android.courtcounter.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.courtcounter.data.CounterContract.CounterEntry;

/**
 * Created by OKHAMAFE EMMANUEL on 5/8/2018.
 */

public class CounterProvider extends ContentProvider{
    public static final String LOG_TAG = CounterProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the counters table */
    private static final int COUNTER = 100;

    /** URI matcher code for the content URI for a single count in the counters table */
    private static final int COUNTER_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(CounterContract.CONTENT_AUTHORITY, CounterContract.PATH_COUNTER, COUNTER);

        sUriMatcher.addURI(CounterContract.CONTENT_AUTHORITY, CounterContract.PATH_COUNTER + "/#" , COUNTER_ID);
    }

    /** Database Helper Object */
    private CounterDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new CounterDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);

        switch (match) {
            case COUNTER:
                cursor = database.query(
                        CounterEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case COUNTER_ID:
                selection = CounterEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(
                        CounterEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            default:
                throw new IllegalArgumentException("Cannot query Unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case COUNTER:
                return CounterEntry.CONTENT_LIST_TYPE;
            case COUNTER_ID:
                return CounterEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case COUNTER:
                return insertCount(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertCount(Uri uri, ContentValues contentValues){
        // Check that the name is not null
        String teamAScore = contentValues.getAsString(CounterEntry.COLUMN_TEAM_A_SCORE);
        if (teamAScore == null) {
            throw new IllegalArgumentException("Team A requires a value");
        }

        String teamBScore = contentValues.getAsString(CounterEntry.COLUMN_TEAM_B_SCORE);
        if (teamBScore == null) {
            throw new IllegalArgumentException("Team B requires a value");
        }

        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(CounterEntry.TABLE_NAME, null, contentValues);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case COUNTER:
                rowsDeleted = db.delete(CounterEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case COUNTER_ID:
                selection = CounterEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(CounterEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported");
        }

        // Notify all listeners that the data at change at the give URI
        if (rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case COUNTER:
                return updateCounter(uri, values, selection, selectionArgs);
            case COUNTER_ID:
                selection = CounterEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
                return updateCounter(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateCounter(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        if (values.containsKey(CounterEntry.COLUMN_TEAM_A_SCORE)){
            int teamAScore = values.getAsInteger(CounterEntry.COLUMN_TEAM_A_SCORE);
            if (teamAScore < 0){
                throw new IllegalArgumentException("Scores can not be less than 0");
            }
        }

        if (values.containsKey(CounterEntry.COLUMN_TEAM_B_SCORE)){
            int teamBScore = values.getAsInteger(CounterEntry.COLUMN_TEAM_B_SCORE);
            if (teamBScore < 0){
                throw new IllegalArgumentException("Scores can not be less than 0");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = db.update(CounterEntry.TABLE_NAME, values, selection, selectionArgs);
        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows updated
        return rowsUpdated;

    }
}
