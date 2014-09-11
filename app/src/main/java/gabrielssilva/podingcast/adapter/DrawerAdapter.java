package gabrielssilva.podingcast.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import gabrielssilva.podingcast.app.R;

public class DrawerAdapter extends BaseAdapter {

    private Context context;
    private String[] items = {"List", "Player"};

    private class ListItemViewHolder {
        TextView itemName;
    }

    public DrawerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int index) {
        return items[index];
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
            view = this.inflateLayout();

            listItemViewHolder.itemName = (TextView) view.findViewById(R.id.text_view);
            view.setTag(listItemViewHolder);
        } else {
            // We can use our Holder!
            listItemViewHolder = (ListItemViewHolder) view.getTag();
        }

        String itemName = items[index];
        listItemViewHolder.itemName.setText(itemName);

        return view;
    }

    private View inflateLayout() {
        LayoutInflater inflater;
        View listItemView;

        inflater = LayoutInflater.from(this.context);
        listItemView = inflater.inflate(R.layout.feed_list_item, null);

        return listItemView;
    }
}
