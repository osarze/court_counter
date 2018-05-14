package com.example.android.courtcounter.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by OKHAMAFE EMMANUEL on 5/8/2018.
 */

public final class CounterContract {
    //Prevent Instantiating
    private CounterContract(){}

    public static String CONTENT_AUTHORITY = "com.example.android.courtcounter";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_COUNTER = "counters";

    public static final class CounterEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_COUNTER);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COUNTER;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COUNTER;

        public static final String TABLE_NAME = "counters";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_TEAM_A_SCORE = "team_a";

        public final static String COLUMN_TEAM_B_SCORE = "team_b";

        public final static String COLUMN_DATE = "date";


    }


}
