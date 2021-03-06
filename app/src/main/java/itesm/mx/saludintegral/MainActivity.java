package itesm.mx.saludintegral;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
 * Main activity that handles fragment transactions to decide what is shown to the user. Also
 * provides with an emergency button. Implements all the fragment interfaces to handle callbacks.
 * @author Mattias Strid, Ruben Garza
 * @version 1
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        EventListFragment.OnFragmentInteractionListener, AddEventFragment.OnEventAddedListener,
        MenuFragment.OnFragmentInteractionListener, ListenerCheckBox,
        EventDetailFragment.OnFragmentInteractionListener, SaludIntegralFragment.OnEventAddedListener {
    private EventOperations dao;
    private boolean inHistoryView; //Probably not the optimal solution but I want to reuse the event list fragment and this was the best solution for navigation issues
    private ArrayList<Event> events = new ArrayList<Event>();

    private Intent notificationIntent;
    private PendingIntent pendingIntent;
    private Event oldEvent;
    NotificationManager notificationManager;

    private static final String MENU_FRAGMENT_TAG = "tagmenu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnEmergencia = (Button) findViewById(R.id.btn_emergencia);
        btnEmergencia.setOnClickListener(this);

        loadMenuFragment();

        dao = new EventOperations(this);
        dao.open();

        notificationIntent = new Intent(getApplicationContext(), NotificationActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0 , notificationIntent, PendingIntent.FLAG_ONE_SHOT);


        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CALL_PHONE},
               0);
    }

    /**
     * Method for handling the click of the emergency button.
     */
    @Override
    public boolean onSupportNavigateUp() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try{
            MenuFragment fragment = (MenuFragment) getFragmentManager().findFragmentByTag(MENU_FRAGMENT_TAG);
            if (fragment.getTag().equals(MENU_FRAGMENT_TAG)){
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
        } catch (Exception e) {
            // Do nothing
        }
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btn_emergencia:
                emergencyCall();
                break;
            default:
                break;
        }
    }

    /**
     * Method for loading the menu layout.
     */
    public void loadMenuFragment(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        Fragment fragment = new MenuFragment();
        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment, MENU_FRAGMENT_TAG);
        transaction.commit();
    }

    /**
     * Method for handling an emergency call, creates an intent to call to 911.
     */
    @SuppressLint("MissingPermission")
    public void emergencyCall() {
        Intent emergencyCall = new Intent(Intent.ACTION_CALL);
        emergencyCall.setData(Uri.parse("tel:911"));
        startActivity(emergencyCall);
    }

    /**
     * Display the agenda with the events desired. Also provide what is going to be the default value
     * of the search field.
     * @param events Events to be displayed
     * @param searchType SearchType to be displayed
     */
    public void loadAgendaFragment(ArrayList<Event> events, int searchType) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        // Only show events from this date and forward
        events = EventHelper.eventsFromDate(events, cal.getTime());
        ArrayList<Integer> separatorSet = new ArrayList<>();
        OrderedEvents orderedEvents = new OrderedEvents(separatorSet, events);
        orderedEvents = EventHelper.turnIntoDateSeparatedList(orderedEvents, false);
        EventListFragment eventListFragment = EventListFragment.newInstance(orderedEvents, false, searchType);
        this.events = events;
        getFragmentManager().beginTransaction().replace(R.id.frame_container, eventListFragment).addToBackStack(null).commit();
    }

    /**
     * Method to set the history fragment. Needs the events to be displayed and the default search.
     * @param events Events to be displayed
     * @param searchType SearchType to be shown
     */
    public void loadHistoryFragment(ArrayList<Event> events, int searchType) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Only display events up until this very moment
        events = EventHelper.eventsToDate(events, new Date());
        ArrayList<Integer> separatorSet = new ArrayList<>();
        OrderedEvents orderedEvents = new OrderedEvents(separatorSet, events);
        orderedEvents = EventHelper.turnIntoDateSeparatedList(orderedEvents, true);
        EventListFragment eventListFragment = EventListFragment.newInstance(orderedEvents, true, searchType);
        this.events= events;
        getFragmentManager().beginTransaction().replace(R.id.frame_container, eventListFragment).addToBackStack(null).commit();
    }

    /**
     * Method to get the events from the database
     * @return ArrayList of events
     */
    public ArrayList<Event> getEvents() {
        ArrayList<Event> eventList = dao.getAllEvents();
        if (eventList!=null){
            return eventList;
        } else {
            return null;
        }
    }

    /**
     * Method to get the events from the database for a specific type
     * @param type Type of events to search for
     * @return ArrayList of events
     */
    public ArrayList<Event> getEventsOfType(int type) {
        ArrayList<Event> eventList = dao.getAllEventsOfType(type);
        if (eventList!=null){
            return eventList;
        } else {
            return null;
        }
    }

    /**
     * Method to handle clicks on the add event button in the agenda fragment.
     */
    @Override
    public void onEventAddButtonClicked() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AddEventFragment addEventFragment = AddEventFragment.newInstance(null, Event.GENERAL);
        getFragmentManager().beginTransaction().replace(R.id.frame_container, addEventFragment).addToBackStack(null).commit();
    }

    /**
     * Method to handle clicks on the add food button in the plan salud integral fragment.
     */
    @Override
    public void onEventAddFood() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AddEventFragment addEventFragment = AddEventFragment.newInstance(null, Event.FOOD);
        getFragmentManager().beginTransaction().replace(R.id.frame_container, addEventFragment).addToBackStack(null).commit();
    }

    @Override
    public void onPillButtonClicked() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AddEventFragment addEventFragment = AddEventFragment.newInstance(null, Event.PILL);
        getFragmentManager().beginTransaction().replace(R.id.frame_container, addEventFragment).addToBackStack(null).commit();
    }

    /**
     * Method to handle clicks on the add exercise button in the plan salud integral fragment.
     */
    @Override
    public void onEventAddExercise() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AddEventFragment addEventFragment = AddEventFragment.newInstance(null, Event.EXERCISE);
        getFragmentManager().beginTransaction().replace(R.id.frame_container, addEventFragment).addToBackStack(null).commit();
    }

    /**
     * Method to handle clicks on the add event button for mental type
     */
    @Override
    public void onEventAddMental() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AddEventFragment addEventFragment = AddEventFragment.newInstance(null, Event.GENERAL);
        getFragmentManager().beginTransaction().replace(R.id.frame_container, addEventFragment).addToBackStack(null).commit();
    }

    @Override
    public void onEventItemClicked(Event event) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EventDetailFragment eventDetailFragment = EventDetailFragment.newInstance(event);
        getFragmentManager().beginTransaction().replace(R.id.frame_container, eventDetailFragment).addToBackStack(null).commit();
    }

    /**
     * This method handles what happens when the user successfully adds an event. The event is added to
     * the database and the user is returned to the appliance list view.
     * @param date Date of event
     * @param title Title of event
     * @param information Information about event
     * @param repeat Information about the repetition of event
     * @param finalDate Final date in case it is going to be repeated
     * @param isModifying Boolean that keeps track of whether the event is a new one or if it is
     *                    being modified
     * @param type Type of event, see Event class
     * @see Event
     */
    @Override
    public void onEventAdded(Date date, String title, String information, int repeat, Date finalDate, boolean isModifying, int type) {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if (isModifying) {
            boolean result = dao.deleteEvent(oldEvent.getId());
            long id = oldEvent.getId();
            long time = oldEvent.getDate().getTime();
            if (result) {
                Toast.makeText(this, "Exito", Toast.LENGTH_LONG).show();
                cancelNotification(getNotification("Notificacion cancelada", "Esta notificación ha sido cancelada."), time, id);
            } else {
                Toast.makeText(this, "Falló", Toast.LENGTH_LONG).show();
            }
        }
        if (repeat != 0) {
            Calendar finalCal = Calendar.getInstance();
            finalCal.setTime(finalDate);
            finalCal.add(Calendar.DAY_OF_YEAR, 1);
            finalDate = finalCal.getTime();
            Calendar calNextDate = Calendar.getInstance();
            calNextDate.setTime(date);
            do {
                Event event = new Event(calNextDate.getTime(), title, information, type);
                long id = dao.addEvent(event);
                event.setId(id);
                events.add(event);
                scheduleNotification(getNotification(title, information), calNextDate.getTime().getTime(), id);
                calNextDate.add(repeat, 1);
            } while (calNextDate.getTime().getTime() < finalDate.getTime());
        } else {
            Event event = new Event(date, title, information, type);
            long id = dao.addEvent(event);
            event.setId(id);
            events.add(event);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd/MM/yyyy HH:mm");

            Toast.makeText(this, simpleDateFormat.format(event.getDate()),Toast.LENGTH_LONG).show();
            scheduleNotification(getNotification(title, information), date.getTime(), id);
        }
        //If succesfully added a new event remove it from the backstack
        FragmentManager manager = getFragmentManager();
        manager.popBackStack();
        manager.popBackStack();
        events = getEvents();
        loadAgendaFragment(events, 0);
    }

    /**
     * Method for scheduling the notification, need to send the notification, date and id.
     */
    private void scheduleNotification(Notification notification, long date , long id ) {

        Intent notificationIntent = new Intent(this, NotifReceiver.class);
        notificationIntent.putExtra(NotifReceiver.NOTIFICATION_ID, id);
        notificationIntent.putExtra(NotifReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, date, pendingIntent);
    }

    /**
     * Method for canceling the notification according to the id that you send
     */
    private void cancelNotification(Notification notification, long date , long id ) {

        Intent notificationIntent = new Intent(this, NotifReceiver.class);
        notificationIntent.putExtra(NotifReceiver.NOTIFICATION_ID, id);
        notificationIntent.putExtra(NotifReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    /**
     * Method for creating the notification according to the info that you send to the method.
     */
    private Notification getNotification(String title, String information) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(title);
        builder.setContentText(information);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setSmallIcon(R.drawable.cell_shape);
        builder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE);
        return builder.build();
    }


    /**
     * Method for handling a pause of the application. Close the database.
     */
    @Override
    protected void onPause() {
        dao.close();
        super.onPause();
    }

    /**
     * When coming back from a pause, open the database so that it can be used again.
     */
    @Override
    protected void onResume() {
        dao.open();
        super.onResume();
    }

    /* Callbacks from the menu fragment */

    /**
     * Method to show the agenda
     */
    @Override
    public void onAgendaButtonClicked() {
        inHistoryView = false;
        events = getEvents();
        loadAgendaFragment(events, 0);
    }

    /**
     * Method to show the sudoku
     */
    @Override
    public void onSudokuButtonClicked() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SudokuFragment fragment = new SudokuFragment();
        getFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();

    }

    /**
     * Method to show the history fragment
     */
    @Override
    public void onHistoryButtonClicked() {
        inHistoryView = true;
        events = getEvents();
        loadHistoryFragment(events, 0);
    }

    /**
     * Method to open the plan salud integral section
     */
    @Override
    public void onPlanButtonClicked() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SaludIntegralFragment saludIntegral = new SaludIntegralFragment();
        getFragmentManager().beginTransaction().replace(R.id.frame_container, saludIntegral).addToBackStack(null).commit();
    }

    /**
     * Method to handle the modification of an event. Opens the add event fragment with the fiels
     * pre filled.
     * @param event Event to be modified
     */
    @Override
    public void onModifyEvent(Event event) {
        oldEvent = event;
        AddEventFragment addEventFragment = AddEventFragment.newInstance(event, event.getType());
        getFragmentManager().beginTransaction().replace(R.id.frame_container, addEventFragment).addToBackStack(null).commit();
    }

    /**
     * Method to handle clicks on the delete button of an event
     * @param event Event to be deleted
     */
    @Override
    public void onDeleteEvent(Event event) {
        boolean result = dao.deleteEvent(event.getId());
        long id = event.getId();
        long date = event.getDate().getTime();
        if (result) {
            Toast.makeText(this, "Exito", Toast.LENGTH_LONG).show();
            cancelNotification(getNotification("Notificación cancelada", "Esta notificacion ha sido cancelada"), date, id);
        } else {
            Toast.makeText(this, "Falló", Toast.LENGTH_LONG).show();
        }
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack();
        fragmentManager.popBackStack();
        if (inHistoryView){
            events = getEvents();
            loadHistoryFragment(events, 0);
        } else {
            events = getEvents();
            loadAgendaFragment(events, 0);
        }
    }

    /**
     * Method to close the detailed view of an event and go back to the list.
     */
    @Override
    public void onOkay() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack();
        fragmentManager.popBackStack();
        events = getEvents();
        loadAgendaFragment(events, 0);
    }

    /**
     * Method to handle clicks on the checkbox in an event list.
     * @param position Position of event in the list
     * @param isChecked Boolean that contains information about the checkbox state
     */
    @Override
    public void onEventChecked(int position, boolean isChecked) {
        Toast.makeText(this, "Cambiado", Toast.LENGTH_LONG).show();
        Event event = events.get(position);
        event.setDone(isChecked);
        boolean result = dao.deleteEvent(event.getId());
        long id = dao.addEvent(event);
        event.setId(id);
    }

    /**
     * Method to handle when the user changes the search terms in an event list
     * @param type Type to be searched for
     */
    @Override
    public void onSearchUpdated(int type) {
        if (type != 0) { // If 0, it's equal to seeing all events
            events = getEventsOfType(type-1);
        } else {
            events = getEvents();
        }
        getFragmentManager().popBackStack();
        if (inHistoryView){
            loadHistoryFragment(events, type);
        } else {
            loadAgendaFragment(events, type);
        }
    }
}

/*
    This code was created by Rubén Garza, Mattias Strid, Ivan Escalante and Juan Pablo Garcia
 */
