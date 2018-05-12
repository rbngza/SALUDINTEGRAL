package itesm.mx.saludintegral;

import java.util.ArrayList;
import java.util.TreeSet;

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
 * Class to store information about event lists, including separators.
 * @author Mattias Strid
 * @version 1
 */
public class OrderedEvents {
    private ArrayList<Integer> separatorSet;
    private ArrayList<Event> events;

    public OrderedEvents(ArrayList<Integer> separatorSet, ArrayList<Event> events) {
        this.separatorSet = separatorSet;
        this.events = events;
    }

    /* Getters and Setters */
    public ArrayList<Integer> getSeparatorSet() { return separatorSet; }
    public void setSeparatorSet(ArrayList<Integer> separatorSet) { this.separatorSet = separatorSet; }

    public ArrayList<Event> getEvents() { return events; }
    public void setEvents(ArrayList<Event> events) { this.events = events; }
}
