package itesm.mx.saludintegral;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
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

public class AddEventFragment extends Fragment implements Spinner.OnItemSelectedListener, TimePickerDialog.OnTimeSetListener, View.OnClickListener{
    protected int fromYear;
    private int fromMonth;
    private int fromDayOfMonth;
    private int hour;
    private int minute;
    private int toYear;
    private int toMonth;
    private int toDayOfMonth;
    private Date date;
    private Date finalDate;
    private AutoCompleteTextView etTitle;
    private TextView tvTime;
    private TextView tvDate;
    private EditText etInformation;
    private Spinner spinner;
    private TextView tvFinalDateText;
    private TextView tvFinalDateDisplay;
    private static final int DATE_PICKER_TO = 0;
    private static final int DATE_PICKER_FROM = 1;

    private OnEventAddedListener mListener;

    DatePickerDialog.OnDateSetListener from_dateListener, to_dateListener;

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

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);
        Button btnSave = view.findViewById(R.id.button_save_event);
        etTitle = view.findViewById(R.id.edit_event_title);
        tvDate = view.findViewById(R.id.text_date);
        tvTime = view.findViewById(R.id.text_time);
        etInformation = view.findViewById(R.id.edit_event_information);
        tvFinalDateText = view.findViewById(R.id.text_final_date);
        tvFinalDateDisplay = view.findViewById(R.id.text_display_final_date);
        tvFinalDateDisplay.setVisibility(View.GONE);
        tvFinalDateText.setVisibility(View.GONE);
        tvFinalDateDisplay.setOnClickListener(this);

        date = new Date();
        finalDate = date;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        this.fromYear = cal.get(Calendar.YEAR);
        this.fromMonth = cal.get(Calendar.MONTH);
        this.fromDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        this.hour = cal.get(Calendar.HOUR);
        this.minute = cal.get(Calendar.MINUTE);
        this.toYear = cal.get(Calendar.YEAR);
        this.toMonth = cal.get(Calendar.MONTH);
        this.toDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd/MM-yyyy");
        tvDate.setText(simpleDateFormat.format(date));
        simpleDateFormat = new SimpleDateFormat("HH:mm");
        tvTime.setText(simpleDateFormat.format(date));

        btnSave.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        tvTime.setOnClickListener(this);

        spinner = (Spinner) view.findViewById(R.id.spinner_repeat);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_repeat, R.layout.spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        simpleDateFormat = new SimpleDateFormat("EEEE dd/MM-yyyy");
        tvFinalDateDisplay.setText(simpleDateFormat.format(finalDate));
        tvFinalDateText.setText(R.string.final_date);

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
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd/MM-yyyy");
                tvFinalDateDisplay.setText(simpleDateFormat.format(finalDate));
            }
        };

        return view;
    }

    protected DatePickerDialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_FROM:
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), from_dateListener, fromYear, fromMonth, fromDayOfMonth);
                datePickerDialog.setTitle("Choose end date");
                return datePickerDialog;
            case DATE_PICKER_TO:
                return new DatePickerDialog(getActivity(), to_dateListener, toYear, toMonth, toDayOfMonth);
        }
        return null;
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
                    mListener.onEventAdded(date, etTitle.getText().toString(), etInformation.getText().toString(), repeat, finalDate);
                }
                break;
            case R.id.text_date:
                DatePickerDialog dateDialog = onCreateDialog(DATE_PICKER_FROM);
                dateDialog.show();
                break;
            case R.id.text_time:
                TimePickerDialog timeDialog = new TimePickerDialog(getActivity(), this, hour, minute, true);
                timeDialog.show();
                break;
            case R.id.text_display_final_date:
                DatePickerDialog dateToDialog = onCreateDialog(DATE_PICKER_TO);
                dateToDialog.show();
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
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
        Date date = new GregorianCalendar(fromYear, fromMonth, fromDayOfMonth).getTime();
        long dateTime = date.getTime();
        this.date.setTime(dateTime+(hour*60+minute)*60*1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        tvTime.setText(simpleDateFormat.format(this.date));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            tvFinalDateText.setVisibility(View.VISIBLE);
            tvFinalDateDisplay.setVisibility(View.VISIBLE);
            onCreateDialog(DATE_PICKER_TO).show();
        } else {
            tvFinalDateDisplay.setVisibility(View.GONE);
            tvFinalDateText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //
    }

    public interface OnEventAddedListener {
        void onEventAdded(Date date, String title, String information, int repeat, Date finalDate);
    }
}
