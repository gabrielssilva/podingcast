package gabrielssilva.podingcast.events;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;

import gabrielssilva.podingcast.app.PlayerEventListener;
import gabrielssilva.podingcast.app.R;
import gabrielssilva.podingcast.controller.ServiceController;

public class PlayPauseClick implements View.OnClickListener {

    private PlayerEventListener eventListener;
    private ServiceController serviceController;

    public PlayPauseClick(PlayerEventListener eventListener, ServiceController serviceController) {
        this.eventListener = eventListener;
        this.serviceController = serviceController;
    }

    @Override
    public void onClick(View view) {
        Button button = (Button) view;

        this.setAttributes(button);
    }

    public void setAttributes(Button button) {
        boolean playing = this.serviceController.playOrPause();
        Resources resources = this.eventListener.getResources();
        String contentDescription;
        Drawable icon;

        if (playing) {
            icon = resources.getDrawable(R.drawable.pause);
            contentDescription = "Pause";
        } else {
            icon = resources.getDrawable(R.drawable.play);
            contentDescription = "Play";
        }

        button.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
        button.setContentDescription(contentDescription);
    }
}