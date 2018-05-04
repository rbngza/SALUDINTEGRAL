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
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        EventListFragment.OnFragmentInteractionListener, AddEventFragment.OnEventAddedListener,
        MenuFragment.OnFragmentInteractionListener, ListenerCheckBox,
        EventDetailFragment.OnFragmentInteractionListener {
    private EventOperations dao;
    private boolean inHistoryView; //Probably not the optimal solution but I want to reuse the event list fragment and this was the best solution for navigation issues
    private ArrayList<Event> events;

    private Intent notificationIntent;
    private PendingIntent pendingIntent;
    private Event oldEvent;
    NotificationManager notificationManager;

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

    public void loadMenuFragment(){
        Fragment fragment = new MenuFragment();
        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    @SuppressLint("MissingPermission")
    public void emergencyCall() {
        Intent emergencyCall = new Intent(Intent.ACTION_CALL);
        emergencyCall.setData(Uri.parse("tel:911"));
        startActivity(emergencyCall);
    }

    public void loadAgendaFragment() {
        events = getEvents();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        events = EventHelper.eventsFromDate(events, cal.getTime());
        ArrayList<Integer> separatorSet = new ArrayList<>();
        OrderedEvents orderedEvents = new OrderedEvents(separatorSet, events);
        orderedEvents = EventHelper.turnIntoDateSeparatedList(orderedEvents, false);
        EventListFragment eventListFragment = EventListFragment.newInstance(orderedEvents, false);
        getFragmentManager().beginTransaction().replace(R.id.frame_container, eventListFragment).addToBackStack(null).commit();
    }

    public void loadHistoryFragment() {
        events = getEvents();
        events = EventHelper.eventsToDate(events, new Date());
        ArrayList<Integer> separatorSet = new ArrayList<>();
        OrderedEvents orderedEvents = new OrderedEvents(separatorSet, events);
        orderedEvents = EventHelper.turnIntoDateSeparatedList(orderedEvents, true);
        EventListFragment eventListFragment = EventListFragment.newInstance(orderedEvents, true);
        getFragmentManager().beginTransaction().replace(R.id.frame_container, eventListFragment).addToBackStack(null).commit();
    }

    //Method to get the events from the database
    public ArrayList<Event> getEvents() {
        ArrayList<Event> eventList = dao.getAllEvents();
        if (eventList!=null){
            return eventList;
        } else {
            return null;
        }
    }

    @Override
    public void onEventAddButtonClicked() {
        AddEventFragment addEventFragment = AddEventFragment.newInstance(null);
        getFragmentManager().beginTransaction().replace(R.id.frame_container, addEventFragment).addToBackStack(null).commit();
    }

    @Override
    public void onEventItemClicked(Event event) {
        EventDetailFragment eventDetailFragment = EventDetailFragment.newInstance(event);
        getFragmentManager().beginTransaction().replace(R.id.frame_container, eventDetailFragment).addToBackStack(null).commit();
    }

    /*
     * This one handles what happens when the user successfully adds an event. The event is added to
     * the database and the user is returned to the appliance list view.
     */
    @Override
    public void onEventAdded(Date date, String title, String information, int repeat, Date finalDate, boolean isModifying) {
        if (isModifying) {
            boolean result = dao.deleteEvent(oldEvent.getId());
            long id = oldEvent.getId();
            long time = oldEvent.getDate().getTime();
            if (result) {
                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
                cancelNotification(getNotification("Canceled notification", "This notification has been canceled."), time, id);
            } else {
                Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
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
                Event event = new Event(calNextDate.getTime(), title, information);
                long id = dao.addEvent(event);
                event.setId(id);
                events.add(event);
                calNextDate.add(repeat, 1);
                scheduleNotification(getNotification(title, information), calNextDate.getTime().getTime(), id);
            } while (calNextDate.getTime().getTime() < finalDate.getTime());
        } else {
            Event event = new Event(date, title, information);
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
        loadAgendaFragment();
    }


    private void scheduleNotification(Notification notification, long date , long id ) {

        Intent notificationIntent = new Intent(this, NotifReceiver.class);
        notificationIntent.putExtra(NotifReceiver.NOTIFICATION_ID, id);
        notificationIntent.putExtra(NotifReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, date, pendingIntent);
    }

    private void cancelNotification(Notification notification, long date , long id ) {

        Intent notificationIntent = new Intent(this, NotifReceiver.class);
        notificationIntent.putExtra(NotifReceiver.NOTIFICATION_ID, id);
        notificationIntent.putExtra(NotifReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }


    private Notification getNotification(String title, String information) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(title);
        builder.setContentText(information);
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setSmallIcon(R.drawable.cell_shape);
        builder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE);
        return builder.build();
    }


    //Method for handling a pause of the application. Close the database.
    @Override
    protected void onPause() {
        dao.close();
        super.onPause();
    }

    //When coming back from a pause, open the database so that it can be used again.
    @Override
    protected void onResume() {
        dao.open();
        super.onResume();
    }

    /*
     * Callbacks from the menu fragment
     */
    @Override
    public void onAgendaButtonClicked() {
        inHistoryView = false;
        loadAgendaFragment();
    }

    @Override
    public void onSudokuButtonClicked() {
        SudokuFragment fragment = new SudokuFragment();
        getFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();

    }

    @Override
    public void onHistoryButtonClicked() {
        inHistoryView = true;
        loadHistoryFragment();
    }

    @Override
    public void onPlanButtonClicked() {
        //Show helpful information for planning a healthy lifestyle here
    }

    @Override
    public void onModifyEvent(Event event) {
        oldEvent = event;
        AddEventFragment addEventFragment = AddEventFragment.newInstance(event);
        getFragmentManager().beginTransaction().replace(R.id.frame_container, addEventFragment).addToBackStack(null).commit();
    }

    @Override
    public void onDeleteEvent(Event event) {
        boolean result = dao.deleteEvent(event.getId());
        long id = event.getId();
        long date = event.getDate().getTime();
        if (result) {
            Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
            cancelNotification(getNotification("Canceled notification", "This notification has been canceled."), date, id);
        } else {
            Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
        }
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack();
        fragmentManager.popBackStack();
        if (inHistoryView){
            loadHistoryFragment();
        } else {
            loadAgendaFragment();
        }
    }

    @Override
    public void onOkay() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack();
        fragmentManager.popBackStack();
        loadAgendaFragment();
    }

    @Override
    public void onEventChecked(int position, boolean isChecked) {
        Toast.makeText(this, "changed", Toast.LENGTH_LONG).show();
        Event event = events.get(position);
        event.setDone(isChecked);
        boolean result = dao.deleteEvent(event.getId());
        long id = dao.addEvent(event);
        event.setId(id);
    }
}


