package gabrielssilva.podingcast.events;

import android.view.View;

import gabrielssilva.podingcast.app.PlayerListener;

public class OnJumpAudioPositionClick implements View.OnClickListener {
    private PlayerListener listener;
    private int deltaInMilliseconds;

    public OnJumpAudioPositionClick(PlayerListener listener, int deltaInMilliseconds) {
        this.listener = listener;
        this.deltaInMilliseconds = deltaInMilliseconds;
    }

    @Override
    public void onClick(View view) {
        this.listener.getService().seekToWithDelta(this.deltaInMilliseconds);
    }
}
