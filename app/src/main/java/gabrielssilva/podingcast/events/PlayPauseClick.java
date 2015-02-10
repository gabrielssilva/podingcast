package gabrielssilva.podingcast.events;

import android.view.View;

import gabrielssilva.podingcast.app.interfaces.PlayerEventListener;

public class PlayPauseClick implements View.OnClickListener {

    private PlayerEventListener eventListener;

    public PlayPauseClick(PlayerEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void onClick(View view) {
        this.eventListener.playOrPause();
    }
}