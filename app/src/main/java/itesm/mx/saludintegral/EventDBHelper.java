package itesm.mx.saludintegral;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/* SALUDINTEGRAL - aplicación con el objetivo de asistir a personas independientes de 60 años en adelante
        Copyright (C) 2018 - ITESM

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

/**
 * Class that manages the database for events. It can create and upgrade/downgrade the database.
 * @author Mattias Strid
 * @version 1
 */
public class EventDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "EventDB.db";
    private static final int DATABASE_VERSION = 1;

    public EventDBHelper(Context context) {
        //Create database
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Method to create tables in the database.
     *
     * @param db Database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EVENTS_TABLE = "CREATE TABLE " +
                DataBaseSchema.EventTable.TABLE_NAME +
                "(" +
                DataBaseSchema.EventTable._ID + " INTEGER PRIMARY KEY," +
                DataBaseSchema.EventTable.COLUMN_NAME_TITLE + " TEXT," +
                DataBaseSchema.EventTable.COLUMN_NAME_TIME + " INTEGER," +
                DataBaseSchema.EventTable.COLUMN_NAME_INFORMATION + " TEXT," +
                DataBaseSchema.EventTable.COLUMN_NAME_TYPE + " INTEGER," +
                DataBaseSchema.EventTable.COLUMN_NAME_ISDONE + " INTEGER" +
                ")";
        Log.i("Eventhelper onCreate", CREATE_EVENTS_TABLE);
        db.execSQL(CREATE_EVENTS_TABLE);
    }

    /**
     * Method to upgrade the database (remove all tables and create them again).
     *
     * @param db         Database
     * @param oldVersion version number old database
     * @param newVersion version number new database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DELETE_EVENT_TABLE = "DROP TABLE IF EXISTS  " +
                DataBaseSchema.EventTable.TABLE_NAME;
        db.execSQL(DELETE_EVENT_TABLE);
        onCreate(db);
    }
}
