package gabrielssilva.podingcast.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import gabrielssilva.podingcast.controller.LocalFilesController;
import gabrielssilva.podingcast.controller.PodcastController;
import gabrielssilva.podingcast.model.Podcast;
import gabrielssilva.podingcast.view.SmartImageView;

public class PodcastDetailsActivity extends Activity {

    public static final String ARG_PODCAST = "ARG_PODCAST";

    private Podcast podcast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast_details);

        this.retrieveInfo();
        this.initViews();
    }

    private void retrieveInfo() {
        String podcastName = this.getIntent().getStringExtra(ARG_PODCAST);
        LocalFilesController localFilesController = new LocalFilesController(this);
        this.podcast = localFilesController.getPodcast(podcastName);
    }

    private void initViews() {
        SmartImageView podcastCover = (SmartImageView) this.findViewById(R.id.podcast_cover);
        podcastCover.setSource(this.podcast.getPodcastName(), this.podcast.getImageAddress());

        TextView podcastNameView = (TextView) this.findViewById(R.id.podcast_name);
        podcastNameView.setText(this.podcast.getPodcastName());

        TextView podcastAddrView = (TextView) this.findViewById(R.id.podcast_address);
        podcastAddrView.setText(this.podcast.getRssAddress());

        TextView podcastEpisodesView = (TextView) this.findViewById(R.id.num_episodes);
        String episodesLabel = getString(R.string.podcast_details_episodes);
        podcastEpisodesView.setText(episodesLabel + " " + this.podcast.getNumberOfEpisodes());

        ImageButton deleteButton = (ImageButton) this.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePodcast();
            }
        });
    }

    private void deletePodcast() {
        PodcastController podcastController = new PodcastController(null);
        podcastController.removePodcast(this, this.podcast);

        onBackPressed();
    }
}
