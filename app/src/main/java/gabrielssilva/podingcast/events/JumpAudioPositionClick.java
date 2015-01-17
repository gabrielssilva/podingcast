package gabrielssilva.podingcast.events;

import android.view.View;

import gabrielssilva.podingcast.app.PlayerEventListener;

public class JumpAudioPositionClick implements View.OnClickListener {
    private PlayerEventListener listener;
    private int deltaInMilliseconds;

    public JumpAudioPositionClick(PlayerEventListener listener, int deltaInMilliseconds) {
        this.listener = listener;
        this.deltaInMilliseconds = deltaInMilliseconds;
    }

    @Override
    public void onClick(View view) {
        this.listener.getService().seekToWithDelta(this.deltaInMilliseconds);
    }
}
