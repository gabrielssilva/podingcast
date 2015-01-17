package gabrielssilva.podingcast.events;

import android.view.View;

import gabrielssilva.podingcast.service.ServiceListener;

public class JumpAudioPositionClick implements View.OnClickListener {
    private ServiceListener serviceListener;
    private int deltaInMilliseconds;

    public JumpAudioPositionClick(ServiceListener serviceListener, int deltaInMilliseconds) {
        this.serviceListener = serviceListener;
        this.deltaInMilliseconds = deltaInMilliseconds;
    }

    @Override
    public void onClick(View view) {
        this.serviceListener.getService().seekToWithDelta(deltaInMilliseconds);
    }
}