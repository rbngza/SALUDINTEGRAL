package itesm.mx.saludintegral;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Adapter class to display events in a list.
 */

public class EventAdapter extends ArrayAdapter<Event> {
    private ArrayList<Integer> separators;
    private ListenerCheckBox mListenerCheckBox;

    public EventAdapter(Context context, ArrayList<Event> events, ArrayList<Integer> separators, ListenerCheckBox listenerCheckBox) {
        super(context, 0, events);
        this.separators = separators;
        mListenerCheckBox = listenerCheckBox;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Event event = getItem(position);
        if (!separators.contains(position)) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_event, parent, false);

            TextView tvInformation = (TextView) convertView.findViewById(R.id.text_information);
            TextView tvTime = (TextView) convertView.findViewById(R.id.text_time);
            TextView tvTitle = (TextView) convertView.findViewById(R.id.text_title);

            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("HH:mm");
            tvTime.setText(simpleDateFormat.format(event.getDate()));
            tvTitle.setText(event.getTitle());
            tvInformation.setText(event.getInformation());
            CheckBox checkBox = convertView.findViewById(R.id.checkbox);
            checkBox.setChecked(event.isDone());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int eventPosition = position;
                    int counter = 0;
                    try {
                        while (separators.get(counter) < position) {
                            eventPosition--;
                            counter++;
                        }
                    } catch (Exception e){
                        // if reach end of separators stop
                    }
                    mListenerCheckBox.onEventChecked(eventPosition, isChecked);
                }
            });
            return convertView;
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_date, parent, false);

            TextView tvDate = (TextView) convertView.findViewById(R.id.text_date);
            Calendar dateEvent = Calendar.getInstance();
            dateEvent.setTime(event.getDate());
            Calendar today = Calendar.getInstance();
            today.setTime(new Date());
            Calendar tomorrow = Calendar.getInstance();
            tomorrow.setTime(new Date());
            tomorrow.add(Calendar.DAY_OF_YEAR, 1);
            if (today.get(Calendar.YEAR) == dateEvent.get(Calendar.YEAR) &&
                    today.get(Calendar.DAY_OF_YEAR) == dateEvent.get(Calendar.DAY_OF_YEAR)) {
                tvDate.setText(R.string.today);
            } else if (tomorrow.get(Calendar.YEAR) == dateEvent.get(Calendar.YEAR) &&
                    tomorrow.get(Calendar.DAY_OF_YEAR) == dateEvent.get(Calendar.DAY_OF_YEAR)) {
                tvDate.setText(R.string.tomorrow);
            } else {
                SimpleDateFormat simpleDateFormat =
                        new SimpleDateFormat("EEEE dd/MM");
                tvDate.setText(simpleDateFormat.format(event.getDate()));
            }
            convertView.setEnabled(false);
            return convertView;
        }
    }
}
