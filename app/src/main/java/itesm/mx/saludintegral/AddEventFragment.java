package itesm.mx.saludintegral;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class AddEventFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, View.OnClickListener{
    private int year;
    private int month;
    private int dayOfMonth;
    private int hour;
    private int minute;
    private Date chosenDateTime;
    private AutoCompleteTextView etTitle;
    private TextView tvTime;
    private TextView tvDate;
    private EditText etInformation;
    private Spinner spinner;

    private OnEventAddedListener mListener;

    public AddEventFragment() {
        // Required empty public constructor
    }

    public static AddEventFragment newInstance() {
        AddEventFragment fragment = new AddEventFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);
        Button btnSave = view.findViewById(R.id.button_save_event);
        etTitle = view.findViewById(R.id.edit_event_title);
        tvDate = view.findViewById(R.id.text_date);
        tvTime = view.findViewById(R.id.text_time);

        chosenDateTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(chosenDateTime);
        this.year = cal.get(Calendar.YEAR);
        this.month = cal.get(Calendar.MONTH);
        this.dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        this.hour = cal.get(Calendar.HOUR);
        this.minute = cal.get(Calendar.MINUTE);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM-yyyy");
        tvDate.setText(simpleDateFormat.format(chosenDateTime));
        simpleDateFormat = new SimpleDateFormat("HH:mm");
        tvTime.setText(simpleDateFormat.format(chosenDateTime));

        btnSave.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        tvTime.setOnClickListener(this);

        spinner = (Spinner) view.findViewById(R.id.spinner_repeat);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_repeat, R.layout.spinner_item);
        spinner.setAdapter(adapter);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_save_event:
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
                    mListener.onEventAdded(chosenDateTime, etTitle.getText().toString(), repeat);
                }
                break;
            case R.id.text_date:
                DatePickerDialog dateDialog = new DatePickerDialog(getActivity(), this, year, month, dayOfMonth);
                dateDialog.show();
                break;
            case R.id.text_time:
                TimePickerDialog timeDialog = new TimePickerDialog(getActivity(), this, hour, minute, true);
                timeDialog.show();
                break;
        }
    }

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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
        long dateTime = date.getTime();
        chosenDateTime.setTime(dateTime+(hour*60+minute)*60*1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM-yyyy");
        tvDate.setText(simpleDateFormat.format(chosenDateTime));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
        Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
        long dateTime = date.getTime();
        chosenDateTime.setTime(dateTime+(hour*60+minute)*60*1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        tvTime.setText(simpleDateFormat.format(chosenDateTime));
    }

    public interface OnEventAddedListener {
        void onEventAdded(Date date, String title, int repeat);
    }
}
