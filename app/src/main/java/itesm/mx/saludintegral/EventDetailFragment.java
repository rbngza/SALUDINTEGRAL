package itesm.mx.saludintegral;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;

/**
 * Fragment to display detailed information about an event and also offer the user to modify it or
 * delete it.
 * @author Mattias Strid
 * @version 1
 */
public class EventDetailFragment extends Fragment implements View.OnClickListener{
    private static final String EVENT_KEY = "event";
    private Event event;

    // Interface for handling the dialog to confirm deletion of an event
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    // Yes button clicked, notify parent
                    mListener.onDeleteEvent(event);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    // No button clicked, do nothing
                    break;
            }
        }
    };

    private OnFragmentInteractionListener mListener;

    public EventDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Instantiation method to create a new fragment. Needs which event is going to be displayed.
     * @param event The event to be displayed
     * @return EventDetailFragment
     */
    public static EventDetailFragment newInstance(Event event) {
        EventDetailFragment fragment = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(EVENT_KEY, event);
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
            event = getArguments().getParcelable(EVENT_KEY);
        }
    }

    /**
     * Method to create the view. Inflates the layout and extracts all the view elements to be used
     * for the displaying of detailed information of an event.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_detail, container, false);
        TextView tvDate = view.findViewById(R.id.text_date);
        TextView tvTime = view.findViewById(R.id.text_time);
        TextView tvTitle = view.findViewById(R.id.text_title);
        TextView tvInformation = view.findViewById(R.id.text_information);
        TextView tvType = view.findViewById(R.id.text_type);
        Button btnModify = view.findViewById(R.id.btn_modify);
        Button btnDelete = view.findViewById(R.id.btn_delete);
        Button btnOk = view.findViewById(R.id.btn_ok);

        btnModify.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnOk.setOnClickListener(this);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd/MM-yyyy");
        tvDate.setText(simpleDateFormat.format(event.getDate()));
        simpleDateFormat = new SimpleDateFormat("HH:mm");
        tvTime.setText(simpleDateFormat.format(event.getDate()));
        tvTitle.setText(event.getTitle());
        tvInformation.setText(event.getInformation());
        tvType.setText(getResources().getStringArray(R.array.spinner_type)[event.getType()]);
        tvInformation.setMovementMethod(new ScrollingMovementMethod());

        /* On some phones, for an unknown reason, the onAttach method is not called when creating
         * fragment, so in that case it is created here in a hardcoded fashion, not optimal but works */
        if (mListener == null) {
            mListener = (OnFragmentInteractionListener) getActivity();
        }
        return view;
    }

    /**
     * Method to handle clicks on the view elements (specifically on the buttons)
     * @param v View object
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete:
                // Confirm that the user wants to delete the event
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.ConfirmMessage).setPositiveButton(R.string.yes, dialogClickListener)
                        .setNegativeButton(R.string.no, dialogClickListener).show();
                break;
            case R.id.btn_modify:
                // Let parent know that the user wants to modify this event
                mListener.onModifyEvent(event);
                break;
            case R.id.btn_ok:
                // Let parent know that the user is done checking this detailed view
                mListener.onOkay();
                break;
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
     * to the parent when the user wants to modify or delete an event or go back to the list.
     */
    public interface OnFragmentInteractionListener {
        void onModifyEvent(Event event);
        void onDeleteEvent(Event event);
        void onOkay();
    }
}
