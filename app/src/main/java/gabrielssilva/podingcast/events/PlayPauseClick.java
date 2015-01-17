package gabrielssilva.podingcast.events;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;

import gabrielssilva.podingcast.app.PlayerEventListener;
import gabrielssilva.podingcast.app.R;
import gabrielssilva.podingcast.service.ServiceListener;

public class PlayPauseClick implements View.OnClickListener {

    private PlayerEventListener eventListener;
    private ServiceListener serviceListener;

    public PlayPauseClick(PlayerEventListener eventListener, ServiceListener serviceListener) {
        this.eventListener = eventListener;
        this.serviceListener = serviceListener;
    }

    @Override
    public void onClick(View view) {
        Button button = (Button) view;

        this.setAttributes(button, serviceListener.getService().isPlaying());
    }

    public void setAttributes(Button button, boolean playing) {
        Drawable icon;
        Resources resources = this.eventListener.getResources();

        if (playing) {
            this.serviceListener.getService().pauseAudio();
            icon = resources.getDrawable(R.drawable.play);

            button.setContentDescription("Play");
            button.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
        } else {
            this.serviceListener.getService().playAudio();
            icon = resources.getDrawable(R.drawable.pause);

            button.setContentDescription("Pause");
            button.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
        }
    }
}