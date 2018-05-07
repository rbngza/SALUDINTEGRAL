package itesm.mx.saludintegral;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class that defines how the database is used.
 * @author Mattias Strid
 * @version 1
 */
public class EventOperations {
    private SQLiteDatabase db;
    private EventDBHelper dbHelper;
    private Event event;

    public EventOperations(Context context) {
        dbHelper = new EventDBHelper(context);
    }

    /**
     * Method to open the database so that it can be manipulated.
     * @throws SQLException
     */
    public void open() throws SQLException {
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            Log.e("SQLOPEN", e.toString());
        }
    }

    /**
     * Method to close the database.
     */
    public void close() {
        db.close();
    }

    /**
     * Method to convert the date to a number that's possible to save in the database...
     * @param date The data to be converted
     * @return Long time in milliseconds
     */
    public static Long persistDate(Date date) {
        if (date != null) {
            return date.getTime();
        }
        return null;
    }

    /**
     * Method to convert time in milliseconds to a date
     * @param time Time in milliseconds
     * @return Date object with time
     */
    public static Date loadDate(Long time) {
        return new Date(time);
    }

    /**
     * Method to add an event to the database.
     * @param event Object to be added
     * @return id of the event in the database
     */
    public long addEvent(Event event) {
        long newRowId = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(DataBaseSchema.EventTable.COLUMN_NAME_TIME, persistDate(event.getDate()));
            values.put(DataBaseSchema.EventTable.COLUMN_NAME_TITLE, event.getTitle());
            values.put(DataBaseSchema.EventTable.COLUMN_NAME_INFORMATION, event.getInformation());
            values.put(DataBaseSchema.EventTable.COLUMN_NAME_TYPE, event.getType());
            if (event.isDone()){
                values.put(DataBaseSchema.EventTable.COLUMN_NAME_ISDONE, 1);
            } else {
                values.put(DataBaseSchema.EventTable.COLUMN_NAME_ISDONE, 0);
            }

            newRowId = db.insert(DataBaseSchema.EventTable.TABLE_NAME, null, values);
        } catch (SQLException e) {
            Log.e("SQLADD", e.toString());
        }
        return newRowId;
    }

    /**
     * Delete an event from the database by searching for its ID.
     * @param id of event
     * @return True if successful, False otherwise
     */
    public boolean deleteEvent(long id) {
        boolean result = false;
        try {db.delete(DataBaseSchema.EventTable.TABLE_NAME,
                        DataBaseSchema.EventTable._ID + " = ?",
                        new String[]{String.valueOf(id)});
            result = true;
        } catch (SQLiteException e) {
            Log.e("SQLDELETE", e.toString());
        }
        return result;
    }

    /**
     * Method to get all the events stored in the database.
     * @return List of events
     */
    public ArrayList<Event> getAllEvents() {
        ArrayList<Event> listEvents = new ArrayList<Event>();
        String selectQuery = "SELECT * FROM " + DataBaseSchema.EventTable.TABLE_NAME + " ORDER BY " + DataBaseSchema.EventTable.COLUMN_NAME_TIME + " DESC";
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    event = new Event(Integer.parseInt(cursor.getString(0)),
                                    loadDate(cursor.getLong(2)),
                                    cursor.getString(1),
                                    cursor.getString(3),
                                    cursor.getInt(4),
                                    cursor.getInt(5)==1);
                    listEvents.add(event);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException e) {
            Log.e("SQLLIST", e.toString());
        }
        return listEvents;
    }

    /**
     * Method to return events of a specific type.
     * @param type Type of events that are desired
     * @return Event list
     */
    public ArrayList<Event> getAllEventsOfType(int type) {
        ArrayList<Event> listEvents = new ArrayList<Event>();
        String selectQuery = "SELECT * FROM " + DataBaseSchema.EventTable.TABLE_NAME + " WHERE " + DataBaseSchema.EventTable.COLUMN_NAME_TYPE +
                "=" + Integer.toString(type) + " ORDER BY " + DataBaseSchema.EventTable.COLUMN_NAME_TIME + " DESC";
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    event = new Event(Integer.parseInt(cursor.getString(0)),
                            loadDate(cursor.getLong(2)),
                            cursor.getString(1),
                            cursor.getString(3),
                            cursor.getInt(4),
                            cursor.getInt(5)==1);
                    listEvents.add(event);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException e) {
            Log.e("SQLLIST", e.toString());
        }
        return listEvents;
    }
}
