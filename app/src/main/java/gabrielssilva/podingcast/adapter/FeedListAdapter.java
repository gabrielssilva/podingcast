package gabrielssilva.podingcast.adapter;

import android.app.LauncherActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

import gabrielssilva.podingcast.app.R;

public class FeedListAdapter extends BaseAdapter {

    private List<File> allFiles;
    private Context context;

    // A holder to store our views.
    private class ListItemViewHolder {
        TextView itemName;
    }

    public FeedListAdapter(Context context, List<File> allFiles) {
        this.context = context;
        this.allFiles = allFiles;
    }

    @Override
    public int getCount() {
        return allFiles.size();
    }

    @Override
    public Object getItem(int index) {
        return allFiles.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {
        ListItemViewHolder listItemViewHolder = new ListItemViewHolder();

        if (view == null) {
            // We need to inflate...
            view = this.inflateLayout(viewGroup);

            listItemViewHolder.itemName = (TextView) view.findViewById(R.id.text_view);
            view.setTag(listItemViewHolder);
        } else {
            // We can use our Holder!
            listItemViewHolder = (ListItemViewHolder) view.getTag();
        }

        String itemName = this.allFiles.get(index).getName();
        listItemViewHolder.itemName.setText(itemName);

        return view;
    }

    private View inflateLayout(ViewGroup viewGroup) {
        LayoutInflater inflater;
        View listItemView;

        inflater = LayoutInflater.from(this.context);
        listItemView = inflater.inflate(R.layout.feed_list_item, null);

        return listItemView;
    }
}
