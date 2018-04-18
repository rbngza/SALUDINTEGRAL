package itesm.mx.saludintegral;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddEventFragment extends Fragment implements View.OnClickListener, DatePickFragment.OnFragmentInteractionListener, TimePickFragment.OnFragmentInteractionListener{
    private int year;
    private int month;
    private int dayOfMonth;
    private int hour;
    private int minute;
    private Date chosenDateTime;
    AutoCompleteTextView etTitle;
    TextView tvTime;
    TextView tvDate;
    private DatePickFragment datePickFragment;

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
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("dd/MM-yyyy");
        tvDate.setText(simpleDateFormat.format(chosenDateTime));
        btnSave.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        simpleDateFormat = new SimpleDateFormat("HH:mm");
        tvTime.setText(simpleDateFormat.format(chosenDateTime));
        datePickFragment = DatePickFragment.newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.time_date_container, datePickFragment).commit();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_save_event:
                if (!etTitle.toString().equals("") && !etTitle.toString().equals(" ")) {
                    mListener.onEventAdded(chosenDateTime, etTitle.getText().toString());
                } else {
                    Toast.makeText(getActivity(), "provide more info", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.text_date:
                datePickFragment = DatePickFragment.newInstance();
                getChildFragmentManager().beginTransaction().replace(R.id.time_date_container, datePickFragment).commit();
                break;
            case R.id.text_time:
                TimePickFragment timePickFragment = TimePickFragment.newInstance();
                getChildFragmentManager().beginTransaction().replace(R.id.time_date_container, timePickFragment).commit();
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

    public interface OnEventAddedListener {
        void onEventAdded(Date date, String title);
    }

    @Override
    public void onDatePicked(int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
        long dateTime = date.getTime();
        chosenDateTime.setTime(dateTime+(hour*60+minute)*60*1000);
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("dd/MM-yyyy");
        tvDate.setText(simpleDateFormat.format(chosenDateTime));
    }

    @Override
    public void onTimeChosen(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
        Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
        long dateTime = date.getTime();
        chosenDateTime.setTime(dateTime+(hour*60+minute)*60*1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        tvTime.setText(simpleDateFormat.format(chosenDateTime));
    }
}
