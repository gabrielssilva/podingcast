package gabrielssilva.podingcast.app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import gabrielssilva.podingcast.controller.EpisodesController;
import gabrielssilva.podingcast.controller.LocalFilesController;
import gabrielssilva.podingcast.helper.Mp3Helper;
import gabrielssilva.podingcast.model.Episode;
import gabrielssilva.podingcast.view.SmartImageView;

public class EpisodeDetailsActivity extends Activity {

    public static final String ARG_EPISODE = "ARG_EPISODE";

    private Episode episode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_details);

        this.retrieveInfo();
        this.initViews();
    }

    private void retrieveInfo() {
        String episodeName = getIntent().getStringExtra(ARG_EPISODE);
        LocalFilesController localFilesController = new LocalFilesController(this);
        this.episode = localFilesController.getEpisode(episodeName);
    }

    private void initViews() {
        boolean isLocal = this.episode.getStatus().equals(Episode.LOCAL);

        this.setImage(isLocal);
        this.setEpisodeDetails();

        ImageButton imageButton = (ImageButton) this.findViewById(R.id.delete_button);
        imageButton.setVisibility(isLocal ? View.VISIBLE : View.GONE);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEpisode();
            }
        });
    }

    private void setImage(boolean isLocal) {
        SmartImageView episodeCover = (SmartImageView) this.findViewById(R.id.episode_cover);

        if (isLocal) {
            Mp3Helper mp3Helper = new Mp3Helper(this.episode.getFilePath());
            Bitmap episodeCoverBitmap = mp3Helper.getEpisodeCover(this.getResources());
            episodeCover.setImageBitmap(episodeCoverBitmap);
        } else {
            episodeCover.setSource(this.episode.getEpisodeName(), this.episode.getCoverAddress());
        }
    }

    private void setEpisodeDetails() {
        TextView episodeNameView = (TextView) this.findViewById(R.id.episode_name);
        episodeNameView.setText(this.episode.getEpisodeName());

        TextView episodeDurationView = (TextView) this.findViewById(R.id.episode_duration);
        episodeDurationView.setText(this.episode.getDuration());

        TextView episodeDescriptionView = (TextView) this.findViewById(R.id.episode_description);
        episodeDescriptionView.setText(Html.fromHtml(this.episode.getDescription()));

        TextView episodeContentView = (TextView) this.findViewById(R.id.episode_content);
        episodeContentView.setText(Html.fromHtml(this.episode.getContent()));
    }

    private void deleteEpisode() {
        EpisodesController episodesController = new EpisodesController(this);
        episodesController.deleteEpisode(this.episode);

        onBackPressed();
    }
}