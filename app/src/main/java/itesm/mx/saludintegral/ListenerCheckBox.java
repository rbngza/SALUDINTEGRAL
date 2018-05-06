package itesm.mx.saludintegral;

/**
 * Interface class to define listeners for checkboxes in the event lists.
 * @author Mattias Strid
 * @version 1
 */
public interface ListenerCheckBox {
    /**
     * Method to be called when a checkbox is modified for a specific event.
     * @param position Position of event in the list
     * @param isChecked Boolean that contains information about the checkbox state
     */
    void onEventChecked(int position, boolean isChecked);
}
