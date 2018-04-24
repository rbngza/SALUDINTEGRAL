package itesm.mx.saludintegral;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TreeSet;

/**
 * Created by matt on 2018-04-15.
 */

public class EventHelper {
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
