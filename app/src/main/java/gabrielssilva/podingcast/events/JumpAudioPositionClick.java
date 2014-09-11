package gabrielssilva.podingcast.events;

import android.view.View;

import gabrielssilva.podingcast.app.PlayerListener;

public class JumpAudioPositionClick implements View.OnClickListener {
    private PlayerListener listener;
    private int deltaInMilliseconds;

    public JumpAudioPositionClick(PlayerListener listener, int deltaInMilliseconds) {
        this.listener = listener;
        this.deltaInMilliseconds = deltaInMilliseconds;
    }

    @Override
    public void onClick(View view) {
        this.listener.getService().seekToWithDelta(this.deltaInMilliseconds);
    }
}
