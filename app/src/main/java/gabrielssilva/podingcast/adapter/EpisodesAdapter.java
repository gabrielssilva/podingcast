package gabrielssilva.podingcast.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import gabrielssilva.podingcast.app.R;
import gabrielssilva.podingcast.model.Episode;
import gabrielssilva.podingcast.model.Podcast;

public class EpisodesAdapter extends BaseAdapter {

    private Context context;
    private Podcast podcast;

    public EpisodesAdapter(Context context, Podcast podcast) {
        this.context = context;
        this.podcast = podcast;
    }

    @Override
    public int getCount() {
        return this.podcast.getEpisodes().size();
    }

    @Override
    public Object getItem(int index) {
        return this.podcast.getEpisodes().get(index);
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

            viewHolder.episodeName = (TextView) view.findViewById(R.id.episode_name);
            viewHolder.episodeDuration = (TextView) view.findViewById(R.id.episode_duration);
            viewHolder.downloadAction = (ImageView) view.findViewById(R.id.action_download);
            viewHolder.itemProgress = (ProgressBar) view.findViewById(R.id.episode_item_progress);
            view.setTag(viewHolder);
        } else {
            // We can use our Holder!
            viewHolder = (ViewHolder) view.getTag();
        }

        Episode episode = this.podcast.getEpisodes().get(index);
        viewHolder.episodeName.setText(episode.getEpisodeName());
        viewHolder.episodeDuration.setText(episode.getDuration());
        viewHolder.downloadAction.setVisibility(episode.getStatus().equals(Episode.NOT_LOCAL) ?
                View.VISIBLE : View.GONE);
        viewHolder.itemProgress.setVisibility(episode.getStatus().equals(Episode.DOWNLOADING)
                ? View.VISIBLE : View.GONE);

        return view;
    }

    private View inflateLayout(ViewGroup viewGroup) {
        LayoutInflater inflater;
        View listItemView;

        inflater = LayoutInflater.from(this.context);
        listItemView = inflater.inflate(R.layout.episodes_list_item, viewGroup, false);

        return listItemView;
    }


    // A holder to store our views.
    private class ViewHolder {
        TextView episodeName;
        TextView episodeDuration;
        ImageView downloadAction;
        ProgressBar itemProgress;
    }
}
