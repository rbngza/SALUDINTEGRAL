package itesm.mx.saludintegral;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


import java.util.ArrayList;

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
 * Fragment for displaying the events. Used both as an agenda and as a history.
 * @author Mattias Strid
 * @version 1
 */
public class EventListFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener{
    private OnFragmentInteractionListener mListener;
    // Keys
    private static final String EVENT_KEY = "events";
    private static final String SEARCH_KEY = "search";
    private static final String SEPARATOR_KEY = "separator";
    private static final String ADD_ENABLED_KEY = "addevents";

    private int searchType;
    private ArrayList<Event> events;
    private ArrayList<Integer> separators;
    private EventAdapter eventAdapter;
    private boolean addEventDisabled;
    private int check =  0;

    public EventListFragment() {
        // Required empty public constructor
    }

    /**
     * Instantiation method to create a new fragment.
     * @param orderedEvents The list of events to be displayed
     * @param addEventDisabled boolean to keep track of if the user should be able to add new events or not from this fragment
     * @param searchType integer corresponding to the type currently searched for, see Event class
     * @return AddEventFragment
     */
    public static EventListFragment newInstance(OrderedEvents orderedEvents, boolean addEventDisabled, int searchType) {
        EventListFragment fragment = new EventListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(EVENT_KEY, orderedEvents.getEvents());
        args.putIntegerArrayList(SEPARATOR_KEY, orderedEvents.getSeparatorSet());
        args.putBoolean(ADD_ENABLED_KEY, addEventDisabled);
        args.putInt(SEARCH_KEY, searchType);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * When creating the fragment get the arguments containing all the information that was passed
     * when instantiated.
     * @param savedInstanceState Bundle containing the information saved in onSaveInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            events = getArguments().getParcelableArrayList(EVENT_KEY);
            separators = getArguments().getIntegerArrayList(SEPARATOR_KEY);
            addEventDisabled = getArguments().getBoolean(ADD_ENABLED_KEY);
            searchType = getArguments().getInt(SEARCH_KEY);
        }
        eventAdapter = new EventAdapter(getActivity(), events, separators, (MainActivity) getActivity());
    }

    /**
     * Method to create the view. Inflates the layout and extracts all the view elements to be used
     * for the listview and search functionality.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* On some phones, for an unknown reason, the onAttach method is not called when creating
         * fragment, so in that case it is created here in a hardcoded fashion, not optimal but works */
        if (mListener == null) {
            mListener = (OnFragmentInteractionListener) getActivity();
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agenda, container, false);

        ListView listView = view.findViewById(R.id.list_agenda);
        listView.setAdapter(eventAdapter);
        listView.setOnItemClickListener(this);
        if (events.size() == 0){
            // Show the textview letting the user know if he doesn´t have events.
            TextView tvnoevents = (TextView) view.findViewById(R.id.text_noevents);
            tvnoevents.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }

        FloatingActionButton floatingActionButton = view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(this);

        Spinner spinnerType = (Spinner) view.findViewById(R.id.spinner_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_search_type, R.layout.spinner_item);
        spinnerType.setAdapter(adapter);
        spinnerType.setSelection(searchType);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Make sure it's not called immediately, might result in an endless loop.
                if (++check>1) {
                    mListener.onSearchUpdated(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (addEventDisabled){
            floatingActionButton.hide();
        }
        return view;
    }

    /**
     * Method to handle clicks on elements in the view.
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                mListener.onEventAddButtonClicked();
        }
    }

    /**
     * Method to handle clicks on items in the list. Notifies parent so detailed information
     * can be displayed about the specific event.
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!separators.contains(position)) {
            mListener.onEventItemClicked(events.get(position));
        }
    }

    /**
     * Method to check that the parents has implemented the interface and create the listener
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Method to detach the listener from the parent
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Interface to be implemented by everyone using this fragment. Allows for callbacks to be made
     * to the parent when the user wants to add an event, when an event is clicked or when the
     * user changes the search.
     */
    public interface OnFragmentInteractionListener {
        void onEventAddButtonClicked();
        void onEventItemClicked(Event event);
        void onSearchUpdated(int type);
    }
}
