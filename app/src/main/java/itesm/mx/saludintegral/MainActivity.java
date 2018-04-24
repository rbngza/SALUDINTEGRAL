package itesm.mx.saludintegral;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity implements View.OnClickListener,
        EventListFragment.OnFragmentInteractionListener, AddEventFragment.OnEventAddedListener,
        MenuFragment.OnFragmentInteractionListener, EventDetailFragment.OnFragmentInteractionListener {
    private EventOperations dao;
    private boolean inHistoryView; //Probably not the optimal solution but I want to reuse the event list fragment and this was the best solution for navigation issues
    private ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnEmergencia = (Button) findViewById(R.id.btn_emergencia);
        btnEmergencia.setOnClickListener(this);

        loadMenuFragment();

        dao = new EventOperations(this);
        dao.open();



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
        events = EventHelper.eventsFromDate(events, new Date());
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
        AddEventFragment addEventFragment = AddEventFragment.newInstance();
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
    public void onEventAdded(Date date, String title, String information, int repeat, Date finalDate) {
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
            } while (calNextDate.getTime().getTime() < finalDate.getTime());
        } else {
            Event event = new Event(date, title, information);
            long id = dao.addEvent(event);
            event.setId(id);
            events.add(event);
        }
        //If succesfully added a new event remove it from the backstack
        FragmentManager manager = getFragmentManager();
        manager.popBackStack();
        manager.popBackStack();
        loadAgendaFragment();
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
        //Start the activity with sudoku
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
        //modify this event in some way
        Toast.makeText(this, "Not implemented yet", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeleteEvent(Event event) {
        boolean result = dao.deleteEvent(event.getId());
        if (result) {
            Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
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
}


