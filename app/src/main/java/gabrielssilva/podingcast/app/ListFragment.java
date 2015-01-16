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
import gabrielssilva.podingcast.controller.FilesList;

public class ListFragment extends Fragment implements EventListener {

    private ListView listView;
    private Activity activity;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        this.rootView = rootView;
        this.activity = getActivity();

        this.initViews();
        this.initListView();

        return rootView;
    }

    private void initViews() {
        this.listView = (ListView) this.rootView.findViewById(R.id.list_view);
    }

    private void initListView() {
        Context context = activity.getApplicationContext();
        FilesList filesList = new FilesList(context);
        FeedListAdapter feedAdapter = new FeedListAdapter(context, filesList.getAllFeeds());

        this.listView.setAdapter(feedAdapter);
    }
}