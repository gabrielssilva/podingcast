package gabrielssilva.podingcast.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import gabrielssilva.podingcast.controller.EpisodesController;
import gabrielssilva.podingcast.controller.LocalFilesController;
import gabrielssilva.podingcast.model.Episode;

public class EpisodeDetailsActivity extends Activity {

    public static final String ARG_EPISODE = "ARG_EPISODE";

    private Episode episode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_details);

        String episodeName = getIntent().getExtras().getString(ARG_EPISODE);
        LocalFilesController localFilesController = new LocalFilesController(this);
        this.episode = localFilesController.getEpisode(episodeName);
        initViews();
    }

    private void initViews() {
        ImageButton imageButton = (ImageButton) this.findViewById(R.id.remove_episode);
        imageButton.setOnClickListener(new RemoveButtonClick());
    }

    private void deleteEpisode() {
        EpisodesController episodesController = new EpisodesController(this);
        episodesController.deleteEpisode(this.episode);

        onBackPressed();
    }


    private class RemoveButtonClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            deleteEpisode();
        }
    }
}
