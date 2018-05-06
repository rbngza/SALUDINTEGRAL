package itesm.mx.saludintegral;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by matt on 2018-04-15.
 */

public class Event implements Parcelable, Comparable{
    private long id;
    private Date date;
    private String title;
    private String information;
    private int type;
    private boolean isDone;

    private static final int GENERAL = 0;
    private static final int PILL = 1;
    private static final int FOOD = 2;
    private static final int EXERCISE = 3;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        public Event[] newArray(int size) { return new Event[size]; }
    };

    public Event(){
        id = 0;
        date = new Date();
        title = "";
        information = "";
        type = GENERAL;
        isDone = false;
    }

    public Event(Date date, String title, String information, int tipo) {
        this.date = date;
        this.title = title;
        this.information = information;
        this.type = tipo;
        this.isDone = false;
    }

    public Event(long id, Date date, String title, String information, int type) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.information = information;
        this.type = type;
        this.isDone = false;
    }

    public Event(long id, Date date, String title, String information, int type, boolean isDone) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.information = information;
        this.type = type;
        this.isDone = isDone;
    }

    public Event(Event event) {
        this.date = event.getDate();
        this.title = event.getTitle();
        this.information = event.getInformation();
        this.type = event.getType();
        this.isDone = event.isDone();
    }

    public Event(Parcel in) {
        this.id = in.readLong();
        this.date = new Date(in.readLong());
        this.title = in.readString();
        this.information = in.readString();
        this.type = in.readInt();
        this.isDone = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(date.getTime());
        dest.writeString(title);
        dest.writeString(information);
        dest.writeInt(type);
        dest.writeByte((byte) (isDone ? 1 : 0));
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getInformation() {
        return information;
    }
    public void setInformation(String information) {
        this.information = information;
    }

    public int getType() { return type;    }
    public void setType(int type) { this.type = type;    }

    public boolean isDone() { return isDone;    }
    public void setDone(boolean done) {isDone = done; }

    @Override
    public int compareTo(@NonNull Object o) {
        try {
            Event event = (Event) o;
            return getDate().compareTo(event.getDate());
        } catch (Exception e) {
            return -1;
        }
    }
}
