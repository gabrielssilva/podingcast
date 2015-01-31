package gabrielssilva.podingcast.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import gabrielssilva.podingcast.adapter.FeedListAdapter;
import gabrielssilva.podingcast.controller.FilesController;
import gabrielssilva.podingcast.events.FeedListItemClick;

public class FeedFragment extends Fragment {

    public final static String ARG_FEED_NAME = "feed_name";

    private ListView listView;
    private Activity activity;
    private ListSelectionListener listSelectionListener;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        this.rootView = rootView;
        this.activity = getActivity();

        this.initViews();
        this.initListView();

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.listSelectionListener = (ListSelectionListener) activity;
    }

    private void initViews() {
        this.listView = (ListView) this.rootView.findViewById(R.id.list_view);
    }

    private void initListView() {
        Context context = activity.getApplicationContext();
        FilesController filesController = new FilesController(context);

        FeedListAdapter feedAdapter = new FeedListAdapter(context, filesController.getAllFeeds());
        FeedListItemClick feedListItemClick = new FeedListItemClick(this.listSelectionListener);

        this.listView.setAdapter(feedAdapter);
        this.listView.setOnItemClickListener(feedListItemClick);
    }
}