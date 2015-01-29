package gabrielssilva.podingcast.events;

import android.view.View;

import gabrielssilva.podingcast.controller.ServiceController;

public class JumpAudioPositionClick implements View.OnClickListener {
    private ServiceController serviceController;
    private int deltaInMilliseconds;

    public JumpAudioPositionClick(ServiceController serviceController, int deltaInMilliseconds) {
        this.serviceController = serviceController;
        this.deltaInMilliseconds = deltaInMilliseconds;
    }

    @Override
    public void onClick(View view) {
        this.serviceController.seekToPosition(deltaInMilliseconds, true);
    }
}