package itesm.mx.saludintegral;

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
 * Interface class to define listeners for checkboxes in the event lists.
 * @author Mattias Strid
 * @version 1
 */
public interface ListenerCheckBox {
    /**
     * Method to be called when a checkbox is modified for a specific event.
     * @param position Position of event in the list
     * @param isChecked Boolean that contains information about the checkbox state
     */
    void onEventChecked(int position, boolean isChecked);
}
