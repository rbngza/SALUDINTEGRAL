package itesm.mx.saludintegral;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends Activity implements View.OnClickListener, AgendaFragment.OnFragmentInteractionListener, AddEventFragment.OnEventAddedListener, MenuFragment.OnFragmentInteractionListener{

    private EventOperations dao;
    ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment = new MenuFragment();
        loadMenu(fragment);

        dao = new EventOperations(this);
        dao.open();

        Button btnEmergencia = (Button) findViewById(R.id.btn_emergencia);
        btnEmergencia.setOnClickListener(this);




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

    public void loadMenu(Fragment fragment){
        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    @SuppressLint("MissingPermission")
    public void emergencyCall() {
        Intent emergencycall = new Intent(Intent.ACTION_CALL);
        emergencycall.setData(Uri.parse("tel:911"));
        startActivity(emergencycall);
    }

    public void addAgendaFragment() {
        events = getEvents();
        ArrayList<Integer> separatorSet = new ArrayList<>();
        OrderedEvents orderedEvents = new OrderedEvents(separatorSet, events);
        orderedEvents = EventHelper.turnIntoDateSeparatedList(orderedEvents);
        AgendaFragment agendaFragment = AgendaFragment.newInstance(orderedEvents);
        getFragmentManager().beginTransaction().replace(R.id.frame_container, agendaFragment).addToBackStack(null).commit();
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
        Toast.makeText(getApplicationContext(), "Event clicked", Toast.LENGTH_LONG).show();
    }

    //This one handles what happens when the user successfully adds an event. The event is added to
    //the database and the user is returned to the appliance list view.
    @Override
    public void onEventAdded(Date date, String title) {
        Event event = new Event(date, title);
        long id = dao.addEvent(event);
        event.setId(id);
        events.add(event);
        addAgendaFragment();
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

    @Override
    public void onCalendarButtonClicked() {
        addAgendaFragment();
    }
}


