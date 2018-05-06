package itesm.mx.saludintegral;

import android.provider.BaseColumns;

/**
 * Class to the describe the database that is used to save events.
 * @author Mattias Strid
 * @version 1
 */
public class DataBaseSchema {
    private DataBaseSchema() {}

    /**
     * Class to implement a table for the events. Contains information about the structure.
     */
    public static class EventTable implements BaseColumns {
        public static final String TABLE_NAME = "events";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_INFORMATION = "information";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_ISDONE = "isDone";
    }
}









