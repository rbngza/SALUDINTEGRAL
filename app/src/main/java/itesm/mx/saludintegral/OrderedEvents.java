package itesm.mx.saludintegral;

import java.util.ArrayList;
import java.util.TreeSet;

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
