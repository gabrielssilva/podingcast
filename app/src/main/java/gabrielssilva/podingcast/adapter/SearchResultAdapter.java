package gabrielssilva.podingcast.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import gabrielssilva.podingcast.app.R;
import gabrielssilva.podingcast.view.SmartImageView;

public class SearchResultAdapter extends BaseAdapter {

    private Context context;
    private JSONObject jsonResult;
    private JSONArray resultArray;

    public SearchResultAdapter(Context context, JSONObject jsonResult) {
        this.context = context;
        this.jsonResult = jsonResult;

        try {
            this.resultArray = jsonResult.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        int count = 0;

        try {
            count = jsonResult.getInt("resultCount");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return count;
    }

    @Override
    public Object getItem(int index) {
        Object item = null;

        try {
            item = this.resultArray.get(index);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return item;
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
            viewHolder.podcastCover = (SmartImageView) view.findViewById(R.id.result_item_cover);
            viewHolder.podcastTitle = (TextView) view.findViewById(R.id.result_item_title);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        try {
            JSONObject resultItem = resultArray.getJSONObject(index);

            viewHolder.podcastTitle.setText(resultItem.getString("collectionName"));
            viewHolder.podcastCover.setSource(resultItem.getString("artworkUrl100"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }


    private View inflateLayout(ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        return inflater.inflate(R.layout.search_result_item, viewGroup, false);
    }


    private class ViewHolder {
        public SmartImageView podcastCover;
        public TextView podcastTitle;
    }
}
