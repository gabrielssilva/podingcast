package gabrielssilva.podingcast.events;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import gabrielssilva.podingcast.app.R;
import gabrielssilva.podingcast.service.PlayerService;

public class OnPlayPauseClick implements View.OnClickListener {

    private PlayerService playerService;
    private Resources resources;

    public OnPlayPauseClick(PlayerService playerService, Resources resources) {
        this.playerService = playerService;
        this.resources = resources;
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        this.setAttributes(button, this.playerService.isPlaying());
    }

    public void setAttributes(Button button, boolean playing) {
        Drawable icon;

        if (playing) {
            this.playerService.pauseAudio();
            icon = this.resources.getDrawable(R.drawable.play);

            button.setContentDescription("Play");
            button.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
        } else {
            this.playerService.playAudio();
            icon = this.resources.getDrawable(R.drawable.pause);

            button.setContentDescription("Pause");
            button.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
        }
    }

}
