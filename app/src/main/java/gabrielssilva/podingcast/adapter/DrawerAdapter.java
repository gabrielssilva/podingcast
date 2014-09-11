package gabrielssilva.podingcast.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import gabrielssilva.podingcast.app.R;

public class DrawerAdapter extends BaseAdapter {

    private Context context;
    private Resources resources;
    private String[] pageNames;

    private class ListItemViewHolder {
        TextView itemName;
    }

    public DrawerAdapter(Context context) {
        this.context = context;
        this.resources = context.getResources();

        this.pageNames = this.resources.getStringArray(R.array.pages);
    }

    @Override
    public int getCount() {
        return pageNames.length;
    }

    @Override
    public Object getItem(int index) {
        return pageNames[index];
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

        String itemName = pageNames[index];
        listItemViewHolder.itemName.setText(itemName);
        this.setIcon(listItemViewHolder.itemName, index);

        return view;
    }

    private View inflateLayout() {
        LayoutInflater inflater;
        View listItemView;

        inflater = LayoutInflater.from(this.context);
        listItemView = inflater.inflate(R.layout.drawer_list_item, null);

        return listItemView;
    }

    private void setIcon(TextView textView, int index) {
        String iconName = getIconName(index);
        int iconID = this.resources.getIdentifier(iconName, null, this.context.getPackageName());
        Drawable icon = this.resources.getDrawable(iconID);

        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
    }

    private String getIconName(int index) {
        String pageName = pageNames[index].toLowerCase();
        String iconName = "drawable/ic_" + pageName;

        return iconName;
    }
}
