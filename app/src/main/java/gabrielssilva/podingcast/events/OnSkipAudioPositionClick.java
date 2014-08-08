package gabrielssilva.podingcast.events;

import android.view.View;

import gabrielssilva.podingcast.app.PlayerListener;

public class OnSkipAudioPositionClick implements View.OnClickListener {
    private PlayerListener listener;

    public OnSkipAudioPositionClick(PlayerListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        this.listener.getService().skipThirtySeconds();
    }
}
