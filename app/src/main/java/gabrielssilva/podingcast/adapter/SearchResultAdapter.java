package gabrielssilva.podingcast.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import gabrielssilva.podingcast.app.R;
import gabrielssilva.podingcast.model.Podcast;
import gabrielssilva.podingcast.view.CachedImageView;

public class SearchResultAdapter extends BaseAdapter {

    private Context context;
    private List<Podcast> podcasts;
    private SparseArray<Bitmap> cachedBitmaps;

    public SearchResultAdapter(Context context, List<Podcast> podcasts) {
        this.context = context;
        this.podcasts = podcasts;
        this.cachedBitmaps = new SparseArray<>();
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
            view = this.inflateLayout(viewGroup);
            viewHolder.podcastCover = (CachedImageView) view.findViewById(R.id.result_item_cover);
            viewHolder.podcastTitle = (TextView) view.findViewById(R.id.result_item_title);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Podcast currentPodcast = podcasts.get(index);
        viewHolder.podcastTitle.setText(currentPodcast.getPodcastName());
        viewHolder.podcastCover.setCachedSource(currentPodcast.getImageAddress(), index,
                this.cachedBitmaps);

        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        this.cachedBitmaps.clear();
        super.notifyDataSetChanged();
    }


    private View inflateLayout(ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        return inflater.inflate(R.layout.search_result_item, viewGroup, false);
    }


    private class ViewHolder {
        public CachedImageView podcastCover;
        public TextView podcastTitle;
    }

}
