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


public class EventListFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener{
    private OnFragmentInteractionListener mListener;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        if (mListener == null) {
            mListener = (OnFragmentInteractionListener) getActivity();
        }

        View view = inflater.inflate(R.layout.fragment_agenda, container, false);

        if (events.size() == 0){
            // Show the tevtview letting the user know if he dosen´t have events.
            TextView tvnoevents = (TextView) view.findViewById(R.id.text_noevents);
            tvnoevents.setVisibility(View.VISIBLE);
        }else {
            // Inflate the layout for this fragment
            ListView listView = view.findViewById(R.id.list_agenda);
            listView.setAdapter(eventAdapter);
            listView.setOnItemClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                mListener.onEventAddButtonClicked();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!separators.contains(position)) {
            mListener.onEventItemClicked(events.get(position));
        }
    }

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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onEventAddButtonClicked();
        void onEventItemClicked(Event event);
        void onSearchUpdated(int type);
    }
}
