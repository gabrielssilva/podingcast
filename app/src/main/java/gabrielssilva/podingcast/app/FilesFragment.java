package gabrielssilva.podingcast.app;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import gabrielssilva.podingcast.adapter.FeedListAdapter;
import gabrielssilva.podingcast.app.interfaces.ListSelectionListener;
import gabrielssilva.podingcast.controller.FilesController;
import gabrielssilva.podingcast.controller.ServiceController;
import gabrielssilva.podingcast.events.FileListItemClick;

public class FilesFragment extends Fragment implements ListSelectionListener {

    private ServiceController serviceController;
    private View rootView;
    private HomeActivity activity;

    private String feedName;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_files, container, false);

        this.rootView = rootView;
        this.activity = (HomeActivity) getActivity();
        this.serviceController = this.activity.getServiceController();

        this.initViews();
        this.retrieveInfo();
        this.initListView();

        return rootView;
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
        FilesController filesController = new FilesController(context);

        FeedListAdapter adapter = new FeedListAdapter(context, filesController.getFeedFiles(this.feedName));
        FileListItemClick listItemClick = new FileListItemClick(this);

        this.listView.setAdapter(adapter);
        this.listView.setOnItemClickListener(listItemClick);
    }


    @Override
    public void onItemSelected(String fileName) {
        ViewPager viewPager = (ViewPager) this.activity.findViewById(R.id.view_pager);

        this.serviceController.playFile(fileName);
        viewPager.setCurrentItem(HomeActivity.PLAYER_FRAGMENT_POS, true);
    }
}