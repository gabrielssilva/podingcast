package gabrielssilva.podingcast.events;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import gabrielssilva.podingcast.app.FeedSelectedListener;
import gabrielssilva.podingcast.app.R;

public class FeedListItemClick implements ListView.OnItemClickListener {

    private FeedSelectedListener feedSelectedListener;

    public FeedListItemClick(FeedSelectedListener feedSelectedListener) {
        this.feedSelectedListener = feedSelectedListener;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
        TextView textView = (TextView) view.findViewById(R.id.feed_name);
        String feedName = textView.getText().toString();

        this.feedSelectedListener.onFeedSelected(feedName);
    }
}
