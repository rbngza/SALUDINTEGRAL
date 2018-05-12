package itesm.mx.saludintegral;

import android.provider.BaseColumns;

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









