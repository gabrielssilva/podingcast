package gabrielssilva.podingcast.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import gabrielssilva.podingcast.app.PodcastDetailsActivity;
import gabrielssilva.podingcast.app.R;
import gabrielssilva.podingcast.model.Podcast;
import gabrielssilva.podingcast.view.SmartImageView;

public class PodcastsAdapter extends BaseAdapter {

    private List<Podcast> podcasts;
    private Context context;

    public PodcastsAdapter(Context context, List<Podcast> podcasts) {
        this.context = context;
        this.podcasts = podcasts;
    }


    @Override
    public int getCount() {
        return podcasts.size();
    }

    @Override
    public Object getItem(int index) {
        return podcasts.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(final int index, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();

        if (view == null) {
            // We need to inflate...
            view = this.inflateLayout(viewGroup);

            viewHolder.podcastName = (TextView) view.findViewById(R.id.feed_podcast_name);
            viewHolder.episodesCount = (TextView) view.findViewById(R.id.feed_episodes_count);
            viewHolder.podcastCover = (SmartImageView) view.findViewById(R.id.podcast_cover);
            viewHolder.infoButton = (ImageButton) view.findViewById(R.id.info_button);
            view.setTag(viewHolder);
        } else {
            // We can use our Holder!
            viewHolder = (ViewHolder) view.getTag();
        }

        final Podcast podcast = this.podcasts.get(index);
        String episodesCountString = this.getEpisodesCountString(podcast.getNumberOfEpisodes());

        viewHolder.podcastName.setText(podcast.getPodcastName());
        viewHolder.episodesCount.setText(episodesCountString);
        viewHolder.podcastCover.setSource(podcast.getPodcastName(), podcast.getImageAddress());

        viewHolder.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PodcastDetailsActivity.class);
                intent.putExtra(PodcastDetailsActivity.ARG_PODCAST, podcast.getPodcastName());
                context.startActivity(intent);
            }
        });

        return view;
    }

    private View inflateLayout(ViewGroup viewGroup) {
        LayoutInflater inflater;
        View listItemView;

        inflater = LayoutInflater.from(this.context);
        listItemView = inflater.inflate(R.layout.podcasts_list_item, viewGroup, false);

        return listItemView;
    }

    private String getEpisodesCountString(int episodeCount) {
        Resources resources = this.context.getResources();
        int stringResId;

        if (episodeCount == 1) {
            stringResId = R.string.feed_episode;
        } else {
            stringResId = R.string.feed_episodes;
        }

        return episodeCount + " " + resources.getString(stringResId);
    }


    // A holder to store our views.
    private class ViewHolder {
        TextView podcastName;
        TextView episodesCount;
        SmartImageView podcastCover;
        ImageButton infoButton;
    }
}