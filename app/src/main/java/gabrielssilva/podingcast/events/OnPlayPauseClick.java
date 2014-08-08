package gabrielssilva.podingcast.events;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import gabrielssilva.podingcast.app.EventListener;
import gabrielssilva.podingcast.app.PlayerListener;
import gabrielssilva.podingcast.app.R;
import gabrielssilva.podingcast.service.PlayerService;

public class OnPlayPauseClick implements View.OnClickListener {

    private PlayerListener listener;

    public OnPlayPauseClick(PlayerListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        this.setAttributes(button, this.listener.getService().isPlaying());
    }

    public void setAttributes(Button button, boolean playing) {
        Drawable icon;
        PlayerService playerService = this.listener.getService();
        Resources resources = this.listener.getResources();

        if (playing) {
            playerService.pauseAudio();
            icon = resources.getDrawable(R.drawable.play);

            button.setContentDescription("Play");
            button.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
        } else {
            playerService.playAudio();
            icon = resources.getDrawable(R.drawable.pause);

            button.setContentDescription("Pause");
            button.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
        }
    }

}
