package gabrielssilva.podingcast.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import gabrielssilva.podingcast.app.R;
import gabrielssilva.podingcast.model.Episode;

public class EpisodesAdapter extends BaseAdapter {

    private Context context;
    private List<Episode> episodes;

    public EpisodesAdapter(Context context, List<Episode> episodes) {
        this.context = context;
        this.episodes = episodes;
    }

    @Override
    public int getCount() {
        return this.episodes.size();
    }

    @Override
    public Object getItem(int index) {
        return this.episodes.get(index);
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
            view.setTag(viewHolder);
        } else {
            // We can use our Holder!
            viewHolder = (ViewHolder) view.getTag();
        }

        Episode episode = this.episodes.get(index);
        viewHolder.episodeName.setText(episode.getEpisodeName());

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
    }
}
