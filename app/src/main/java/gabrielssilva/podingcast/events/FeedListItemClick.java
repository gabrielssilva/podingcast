package gabrielssilva.podingcast.events;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import gabrielssilva.podingcast.app.interfaces.ListSelectionListener;
import gabrielssilva.podingcast.app.R;

public class FeedListItemClick implements ListView.OnItemClickListener {

    private ListSelectionListener listSelectionListener;

    public FeedListItemClick(ListSelectionListener listSelectionListener) {
        this.listSelectionListener = listSelectionListener;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
        TextView textView = (TextView) view.findViewById(R.id.feed_name);
        String fileName = textView.getText().toString();

        this.listSelectionListener.onItemSelected(fileName);
    }
}
