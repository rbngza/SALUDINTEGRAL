package itesm.mx.saludintegral;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class EventDetailFragment extends Fragment implements View.OnClickListener{
    private static final String EVENT_KEY = "event";
    private Event event;

    private OnFragmentInteractionListener mListener;

    public EventDetailFragment() {
        // Required empty public constructor
    }

    public static EventDetailFragment newInstance(Event event) {
        EventDetailFragment fragment = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(EVENT_KEY, event);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event = getArguments().getParcelable(EVENT_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_detail, container, false);
        TextView tvDate = view.findViewById(R.id.text_date);
        TextView tvTime = view.findViewById(R.id.text_time);
        TextView tvTitle = view.findViewById(R.id.text_title);
        TextView tvInformation = view.findViewById(R.id.text_information);
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
        tvInformation.setMovementMethod(new ScrollingMovementMethod());
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete:
                mListener.onDeleteEvent(event);
                break;
            case R.id.btn_modify:
                mListener.onModifyEvent(event);
                break;
            case R.id.btn_ok:
                mListener.onOkay();
                break;
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
        void onModifyEvent(Event event);
        void onDeleteEvent(Event event);
        void onOkay();
    }
}
