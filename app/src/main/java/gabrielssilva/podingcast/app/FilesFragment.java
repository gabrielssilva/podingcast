package gabrielssilva.podingcast.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import gabrielssilva.podingcast.adapter.FeedListAdapter;
import gabrielssilva.podingcast.controller.FilesList;
import gabrielssilva.podingcast.events.FileListItemClick;

public class FilesFragment extends Fragment {

    private View rootView;
    private Activity activity;
    private ListSelectionListener listSelectionListener;

    private String feedName;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_files, container, false);
        this.rootView = rootView;
        this.activity = getActivity();

        this.initViews();
        this.retrieveInfo();
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

    private void retrieveInfo() {
        Bundle bundle = this.getArguments();
        this.feedName = bundle.getString(FeedFragment.ARG_FEED_NAME);
    }

    private void initListView() {
        Context context = activity.getApplicationContext();
        FilesList filesList = new FilesList(context);

        FeedListAdapter adapter = new FeedListAdapter(context, filesList.getFeedFiles(this.feedName));
        FileListItemClick listItemClick = new FileListItemClick(this.listSelectionListener);

        this.listView.setAdapter(adapter);
        this.listView.setOnItemClickListener(listItemClick);
    }
}
