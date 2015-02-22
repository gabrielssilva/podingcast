package gabrielssilva.podingcast.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import gabrielssilva.podingcast.app.R;
import gabrielssilva.podingcast.model.Podcast;

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
    public View getView(int index, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();

        if (view == null) {
            // We need to inflate...
            view = this.inflateLayout(viewGroup);

            viewHolder.podcastName = (TextView) view.findViewById(R.id.feed_podcast_name);
            viewHolder.episodesCount = (TextView) view.findViewById(R.id.feed_episodes_count);
            view.setTag(viewHolder);
        } else {
            // We can use our Holder!
            viewHolder = (ViewHolder) view.getTag();
        }

        Podcast podcast = this.podcasts.get(index);
        viewHolder.podcastName.setText(podcast.getPodcastName());
        viewHolder.episodesCount.setText(podcast.getNumberOfEpisodes()+R.string.feed_episodes);

        return view;
    }

    private View inflateLayout(ViewGroup viewGroup) {
        LayoutInflater inflater;
        View listItemView;

        inflater = LayoutInflater.from(this.context);
        listItemView = inflater.inflate(R.layout.podcasts_list_item, viewGroup, false);

        return listItemView;
    }


    // A holder to store our views.
    private class ViewHolder {
        TextView podcastName;
        TextView episodesCount;
    }
}