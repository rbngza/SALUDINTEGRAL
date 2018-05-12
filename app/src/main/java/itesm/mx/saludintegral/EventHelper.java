package itesm.mx.saludintegral;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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
 * Class with static methods to: create the event lists including the separators for displaying the
 * date in the agenda and history views, discarding events until a specific time, or discarding from
 * a specific time.
 * @author Mattias Strid
 * @version 1
 */
public class EventHelper {
    /**
     * Creates the event list given the parameters
     * @param orderedEvents takes an ordered event object containing information about the events
     *                      and the separators
     * @param reversed boolean to sort the list from old to new, or new to old
     * @return OrderedEvents object with all information
     */
    public static OrderedEvents turnIntoDateSeparatedList(OrderedEvents orderedEvents, boolean reversed) {
        ArrayList<Event> events = orderedEvents.getEvents();
        ArrayList<Integer> separatorSet = orderedEvents.getSeparatorSet();
        if (events.size() > 0) {
            Collections.sort(events);
            if (reversed) {
                Collections.reverse(events);
            }
            ArrayList<Event> temporaryEvents = new ArrayList<>();
            Event event = new Event(events.get(0));
            temporaryEvents.add(event);
            separatorSet.add(0);
            for (int i = 0; i < events.size()-1; i++){
                Event event1 = events.get(i);
                Event event2 = events.get(i+1);
                Calendar dateEvent1 = Calendar.getInstance();
                Calendar dateEvent2 = Calendar.getInstance();
                dateEvent1.setTime(event1.getDate());
                dateEvent2.setTime(event2.getDate());
                temporaryEvents.add(event1);
                if (dateEvent1.get(Calendar.YEAR) != dateEvent2.get(Calendar.YEAR) ||
                        dateEvent1.get(Calendar.DAY_OF_YEAR) != dateEvent2.get(Calendar.DAY_OF_YEAR)) {
                    Event event3 = new Event(event2);
                    temporaryEvents.add(event3);
                    separatorSet.add(temporaryEvents.size()-1);
                }
            }
            temporaryEvents.add(events.get(events.size()-1));
            return new OrderedEvents(separatorSet, temporaryEvents);
        }
        return new OrderedEvents(separatorSet, events);
    }

    /**
     * Returns a list of events from a specific date
     * @param events
     * @param date
     * @return
     */
    public static ArrayList<Event> eventsFromDate(ArrayList<Event> events, Date date){
        ArrayList<Event> eventsFromDate = new ArrayList<>();
        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            if(event.getDate().compareTo(date)>0){
                eventsFromDate.add(event);
            }
        }
        return eventsFromDate;
    }

    /**
     * Returns a list of events to a specific date
     * @param events
     * @param date
     * @return
     */
    public static ArrayList<Event> eventsToDate(ArrayList<Event> events, Date date){
        ArrayList<Event> eventsFromDate = new ArrayList<>();
        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            if(event.getDate().compareTo(date)<0){
                eventsFromDate.add(event);
            }
        }
        return eventsFromDate;
    }
}
