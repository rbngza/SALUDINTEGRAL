package itesm.mx.saludintegral;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
 * Fragment for adding different kinds of events, also used when modifying an event by taking the
 * old event as an argument. Provides the user with an interface for filling in the information
 * about the event and when filled properly notifies the parent about the action by a listener
 * interface that has to be implemented.
 * @author Mattias Strid
 * @version 1
 */
public class AddEventFragment extends Fragment implements Spinner.OnItemSelectedListener, TimePickerDialog.OnTimeSetListener, View.OnClickListener{
    // Variables to keep track of the selected start and end time
    private int fromYear;
    private int fromMonth;
    private int fromDayOfMonth;
    private int hour;
    private int minute;
    private int toYear;
    private int toMonth;
    private int toDayOfMonth;
    private Date date;
    private Date finalDate;

    // Variables holding the different view elements from the layout xml
    private AutoCompleteTextView etTitle;
    private TextView tvTime;
    private TextView tvDate;
    private EditText etInformation;
    private Spinner spinner;
    private Spinner spinnerType;
    private TextView tvFinalDateText;
    private TextView tvFinalDateDisplay;

    // Variable to keep track of what kind of event is being added/modified. Default is general
    private int type = Event.GENERAL;

    private int position;

    // Constants to distinguish between the two date pickers that can be triggered
    private static final int DATE_PICKER_TO = 0;
    private static final int DATE_PICKER_FROM = 1;

    // Keys for passing data
    private static final String TYPE_KEY = "type";
    private static final String EVENT_KEY = "event";

    // Boolean to keep track of if the event is a new one or if it's being modified
    private boolean isModifying;
    // And the belonging event in case of modification
    private Event event;

    // The listener used for callbacks
    private OnEventAddedListener mListener;

    // The listeners for setting the dates
    DatePickerDialog.OnDateSetListener from_dateListener, to_dateListener;

    public AddEventFragment() {
        // Required empty public constructor
    }

    /**
     * Instantiation method to create a new fragment. If desired the user can pass an event which
     * allows for modification of that event, otherwise null is accepted. The user also has to tell
     * which type of event it is, preferably by using the Event class.
     * @param event The (optional) event to be modified. Set to null if adding a new one
     * @param type The type of event, use Event class constants
     * @return AddEventFragment
     */
    public static AddEventFragment newInstance(Event event, int type) {
        AddEventFragment fragment = new AddEventFragment();
        Bundle args = new Bundle();
        args.putInt(TYPE_KEY, type);
        if (event != null) {
            args.putParcelable(EVENT_KEY, event);
        }
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
            if (event == null) {
                isModifying = false;
            } else {
                isModifying = true;
            }
            type = getArguments().getInt(TYPE_KEY);
        }
    }

    /**
     * Method to create the view. Inflates the layout and extracts all the view elements to be used
     * for the adding/modification of an event, which are handled separately.
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
            mListener = (OnEventAddedListener) getActivity();
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);
        // Get all the view elements
        Button btnSave = view.findViewById(R.id.button_save_event);
        etTitle = view.findViewById(R.id.edit_event_title);
        tvDate = view.findViewById(R.id.text_date);
        tvTime = view.findViewById(R.id.text_time);
        etInformation = view.findViewById(R.id.edit_event_information);
        tvFinalDateText = view.findViewById(R.id.text_final_date);
        tvFinalDateDisplay = view.findViewById(R.id.text_display_final_date);

        // By default hide the final date, and only show it when the user want to repeat an event
        tvFinalDateDisplay.setVisibility(View.GONE);
        tvFinalDateText.setVisibility(View.GONE);
        tvFinalDateDisplay.setOnClickListener(this);
        SimpleDateFormat simpleDateFormat;

        // If modifying an event
        if (event != null) {
            date = event.getDate();
            tvFinalDateText.setVisibility(View.INVISIBLE);
            tvFinalDateDisplay.setVisibility(View.INVISIBLE);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            this.fromYear = cal.get(Calendar.YEAR);
            this.fromMonth = cal.get(Calendar.MONTH);
            this.fromDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            this.hour = cal.get(Calendar.HOUR_OF_DAY);
            this.minute = cal.get(Calendar.MINUTE);
            simpleDateFormat = new SimpleDateFormat("EEEE dd/MM-yyyy");
            tvDate.setText(simpleDateFormat.format(date));
            simpleDateFormat = new SimpleDateFormat("HH:mm");
            tvTime.setText(simpleDateFormat.format(date));
            spinner = (Spinner) view.findViewById(R.id.spinner_repeat);
            spinner.setVisibility(View.INVISIBLE);
            etTitle.setText(event.getTitle());
            etInformation.setText(event.getInformation());
            TextView tvRepeat = (TextView) view.findViewById(R.id.text_repeat);
            tvRepeat.setVisibility(View.INVISIBLE);
        } else { // If creating a new event
            date = new Date();
            finalDate = date;
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            this.fromYear = cal.get(Calendar.YEAR);
            this.fromMonth = cal.get(Calendar.MONTH);
            this.fromDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            this.hour = cal.get(Calendar.HOUR_OF_DAY);
            this.minute = cal.get(Calendar.MINUTE);
            this.toYear = cal.get(Calendar.YEAR);
            this.toMonth = cal.get(Calendar.MONTH);
            this.toDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            simpleDateFormat = new SimpleDateFormat("EEEE dd/MM-yyyy");
            tvDate.setText(simpleDateFormat.format(date));
            simpleDateFormat = new SimpleDateFormat("HH:mm");
            tvTime.setText(simpleDateFormat.format(date));
            simpleDateFormat = new SimpleDateFormat("EEE dd/MM-yyyy");
            tvFinalDateDisplay.setText(simpleDateFormat.format(finalDate));
            tvFinalDateText.setText(R.string.final_date);
            spinner = (Spinner) view.findViewById(R.id.spinner_repeat);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.spinner_repeat, R.layout.spinner_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);
        }

        spinnerType = (Spinner) view.findViewById(R.id.spinner_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_type, R.layout.spinner_item);
        spinnerType.setAdapter(adapter);
        spinnerType.setOnItemSelectedListener(this);
        TextView textType = (TextView) view.findViewById(R.id.text_type);

        // If type is not general, don't allow user to modify it
        if (type != 0){
            textType.setText(getResources().getStringArray(R.array.spinner_type)[type]);
            spinnerType.setVisibility(View.GONE);
        } else {
            textType.setVisibility(View.GONE);
        }

        btnSave.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        tvTime.setOnClickListener(this);

        from_dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fromYear = year;
                fromMonth = month;
                fromDayOfMonth = dayOfMonth;
                Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
                long dateTime = date.getTime();
                AddEventFragment.this.date.setTime(dateTime+(hour*60+minute)*60*1000);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd/MM-yyyy");
                tvDate.setText(simpleDateFormat.format(AddEventFragment.this.date));
            }
        };

        to_dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                toYear = year;
                toMonth = month;
                toDayOfMonth = dayOfMonth;
                finalDate = new GregorianCalendar(year, month, dayOfMonth).getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE dd/MM-yyyy");
                tvFinalDateDisplay.setText(simpleDateFormat.format(finalDate));
            }
        };

        return view;
    }

    /**
     * Method to handle the multiple DatePickerDialogs that can be created
     * @param id ID of the datePickerDialog to be created
     * @return DatePickerDialog
     */
    protected DatePickerDialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_FROM:
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), from_dateListener, fromYear, fromMonth, fromDayOfMonth);
                return datePickerDialog;
            case DATE_PICKER_TO:
                DatePickerDialog datePickerDialogTo = new DatePickerDialog(getActivity(), to_dateListener, toYear, toMonth, toDayOfMonth);
                datePickerDialogTo.setTitle("Choose end date");
                if (position <=2) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.YEAR, 3);
                    datePickerDialogTo.getDatePicker().setMaxDate(calendar.getTime().getTime());
                }
                return datePickerDialogTo;
        }
        return null;
    }

    /**
     * Method to handle clicks on the view elements (specifically on the buttons)
     * @param v View object
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_save_event:
                // Check that enough information is provided
                if (etTitle.getText().toString().length() < 1) {
                    Toast.makeText(getActivity(), R.string.missing_information_save_event, Toast.LENGTH_LONG).show();
                } else {
                    int repeat;
                    switch (spinner.getSelectedItemPosition()) {
                        case 0:
                            repeat = 0;
                            break;
                        case 1:
                            repeat = Calendar.DAY_OF_YEAR;
                            break;
                        case 2:
                            repeat = Calendar.WEEK_OF_YEAR;
                            break;
                        case 3:
                            repeat = Calendar.MONTH;
                            break;
                        default:
                            repeat = 0;
                            break;
                    }
                    Toast.makeText(getActivity(), R.string.saving_events, Toast.LENGTH_LONG).show();
                    // Make a callback to parents
                    mListener.onEventAdded(date, etTitle.getText().toString(), etInformation.getText().toString(), repeat, finalDate, isModifying, type);
                }
                break;
            case R.id.text_date:
                DatePickerDialog dateDialog = onCreateDialog(DATE_PICKER_FROM);
                dateDialog.show();
                break;
            case R.id.text_time:
                TimePickerDialog timeDialog = new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
                timeDialog.show();
                break;
            case R.id.text_display_final_date:
                DatePickerDialog dateToDialog = onCreateDialog(DATE_PICKER_TO);
                dateToDialog.show();
        }
    }

    /**
     * Method to check that the parents has implemented the interface and create the listener
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEventAddedListener) {
            mListener = (OnEventAddedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnEventAddedListener");
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
     * Method which is called when the user selects a time in the time picker view, and sets the
     * time for the textView.
     * @param view
     * @param hourOfDay
     * @param minute
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
        Date date = new GregorianCalendar(fromYear, fromMonth, fromDayOfMonth).getTime();
        long dateTime = date.getTime();
        this.date.setTime(dateTime+(hour*60+minute)*60*1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        tvTime.setText(simpleDateFormat.format(this.date));
    }

    /**
     * Method to handle what happens when the user interacts with the spinners in the fragment.
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            // If the user wants to set the repetition they are asked to set a final date
            case R.id.spinner_repeat:
                if (position != 0) {
                    this.position = position;
                    tvFinalDateText.setVisibility(View.VISIBLE);
                    tvFinalDateDisplay.setVisibility(View.VISIBLE);
                    onCreateDialog(DATE_PICKER_TO).show();
                } else {
                    // If the user doesn't want to repeat the event, hide the final date
                    tvFinalDateDisplay.setVisibility(View.GONE);
                    tvFinalDateText.setVisibility(View.GONE);
                }
                break;
            case R.id.spinner_type:
                type = position;
                break;
        }
    }

    /**
     * Method that has to be implemented for the spinner OnItemSelected to work. Does nothing.
     * @param parent
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //
    }

    /**
     * Interface to be implemented by everyone using this fragment. Allows for callbacks to be made
     * to the parent when an event is added.
     */
    public interface OnEventAddedListener {
        void onEventAdded(Date date, String title, String information, int repeat, Date finalDate, boolean isModifying, int type);
    }
}
