package gabrielssilva.podingcast.app.interfaces;

/* Using this interface just to keep the event methods simple.
 * I'll get the value on the Event, and the Activity implementing this interface will handle it.
 * It can be used for pretty much any ListView Item Click
 */
public interface ListSelectionListener {

    public void onItemSelected(String itemName);
}