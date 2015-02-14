package gabrielssilva.podingcast.app.interfaces;

/* To avoid the anonymous nested classes, this interface will route all events from the buttons,
 * so the Activity can implement simple methods to handle them. It also prevents the activity
 * to implement too many interfaces, one for each event type.
 */
public interface PlayerEventListener {

    public void playOrPause();
    public void seekToPosition(int deltaInMilliseconds);

    public void updateSeekBar();
    public void startUpdatingSeekBar(int seekPosition);
    public void stopUpdatingSeekBar();
}