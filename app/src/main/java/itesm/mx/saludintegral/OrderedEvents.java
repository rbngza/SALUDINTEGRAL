package itesm.mx.saludintegral;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by matt on 2018-04-15.
 */

public class OrderedEvents {
    ArrayList<Integer> separatorSet;
    ArrayList<Event> events;

    public OrderedEvents(){
        separatorSet = new ArrayList<>();
        events = new ArrayList<>();
    }

    public OrderedEvents(ArrayList<Integer> separatorSet, ArrayList<Event> events) {
        this.separatorSet = separatorSet;
        this.events = events;
    }

    public ArrayList<Integer> getSeparatorSet() {
        return separatorSet;
    }

    public void setSeparatorSet(ArrayList<Integer> separatorSet) {
        this.separatorSet = separatorSet;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
