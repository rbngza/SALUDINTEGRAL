package itesm.mx.saludintegral;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Class to hold event objects and do manipulation on them. Implements parcelable to be able to be
 * passed between activities and fragments, and comparable to compare events by checking their date.
 * @author Mattias Strid
 * @version 1
 */
public class Event implements Parcelable, Comparable{
    // Variables that hold information about the specific event
    private long id;
    private Date date;
    private String title;
    private String information;
    private int type;
    private boolean isDone;

    // Constants to be used to distinguish between different types of events. Public access.
    public static final int GENERAL = 0;
    public static final int PILL = 1;
    public static final int FOOD = 2;
    public static final int EXERCISE = 3;

    /**
     * Method that has to be implemented for the parceability so that events can be passed around.
     */
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        public Event[] newArray(int size) { return new Event[size]; }
    };

    /**
     * Constructor to create an event without id. Automatically set isDone to false.
     * @param date
     * @param title
     * @param information
     * @param type
     */
    public Event(Date date, String title, String information, int type) {
        this.date = date;
        this.title = title;
        this.information = information;
        this.type = type;
        this.isDone = false;
    }

    /**
     * Constructor to create a new event with all information.
     * @param id
     * @param date
     * @param title
     * @param information
     * @param type
     * @param isDone
     */
    public Event(long id, Date date, String title, String information, int type, boolean isDone) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.information = information;
        this.type = type;
        this.isDone = isDone;
    }

    /**
     * Constructor to copy an event
     * @param event Event object to be copied
     */
    public Event(Event event) {
        this.id = event.getId();
        this.date = event.getDate();
        this.title = event.getTitle();
        this.information = event.getInformation();
        this.type = event.getType();
        this.isDone = event.isDone();
    }

    /**
     * Constructor to create event object from a Parcel.
     * @param in
     */
    public Event(Parcel in) {
        this.id = in.readLong();
        this.date = new Date(in.readLong());
        this.title = in.readString();
        this.information = in.readString();
        this.type = in.readInt();
        this.isDone = in.readByte() != 0;
    }

    /**
     * Method that has to be implemented for its parceability.
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Method to write the contents of an event object to a Parcel.
     * @param dest where to save the information
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(date.getTime());
        dest.writeString(title);
        dest.writeString(information);
        dest.writeInt(type);
        dest.writeByte((byte) (isDone ? 1 : 0)); // Can't store a boolean in a parcel, store a byte
    }

    /* Getters and setters */
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getInformation() { return information; }
    public void setInformation(String information) { this.information = information; }

    public int getType() { return type;    }
    public void setType(int type) { this.type = type;    }

    public boolean isDone() { return isDone;    }
    public void setDone(boolean done) {isDone = done; }

    /**
     * Method to compare two event objects by their date stamp
     * @param o event to be compared with this one
     * @return 0 if equal, negative if this one happens before and positive if after
     */
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
