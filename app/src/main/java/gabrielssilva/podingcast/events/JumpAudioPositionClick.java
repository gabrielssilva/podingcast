package gabrielssilva.podingcast.events;

import android.view.View;

import gabrielssilva.podingcast.app.PlayerEventListener;

public class JumpAudioPositionClick implements View.OnClickListener {

    private PlayerEventListener eventListener;
    private int deltaInMilliseconds;

    public JumpAudioPositionClick(PlayerEventListener eventListener, int deltaInMilliseconds) {
        this.eventListener = eventListener;
        this.deltaInMilliseconds = deltaInMilliseconds;
    }

    @Override
    public void onClick(View view) {
        this.eventListener.seekToPosition(deltaInMilliseconds);
    }
}