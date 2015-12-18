package com.example.olesya.quickpress;

import android.provider.BaseColumns;

/**
 * Created by olesya on 18-Dec-15.
 */
public class TimesContract {
    public TimesContract(){}
    public static abstract class TimesContractEntry implements BaseColumns {
        public static final String TABLE_NAME = "LogTime";
        public static final String RECENT_RESULT = "RecentResult";
        public static final String BEST_RESULT = "BestResult";
        public static final String LEVEL = "Level";
        public static final String COMPLEXITY = "Complexity";
    }
}
