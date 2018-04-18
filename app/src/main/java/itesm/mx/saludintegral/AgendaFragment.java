package itesm.mx.saludintegral;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class AgendaFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener{
    private OnFragmentInteractionListener mListener;
    private static final String EVENT_KEY = "events";
    private static final String SEPARATOR_KEY = "separator";
    private ArrayList<Event> events;
    private ArrayList<Integer> separators;
    private EventAdapter eventAdapter;

    public AgendaFragment() {
        // Required empty public constructor
    }

    public static AgendaFragment newInstance(OrderedEvents orderedEvents) {
        AgendaFragment fragment = new AgendaFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(EVENT_KEY, orderedEvents.getEvents());
        args.putIntegerArrayList(SEPARATOR_KEY, orderedEvents.getSeparatorSet());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            events = getArguments().getParcelableArrayList(EVENT_KEY);
            separators = getArguments().getIntegerArrayList(SEPARATOR_KEY);
        }
        eventAdapter = new EventAdapter(getActivity(), events, separators);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agenda, container, false);
        ListView listView = view.findViewById(R.id.list_agenda);
        listView.setAdapter(eventAdapter);
        listView.setOnItemClickListener(this);

        FloatingActionButton floatingActionButton = view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(this);
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
    }
}
