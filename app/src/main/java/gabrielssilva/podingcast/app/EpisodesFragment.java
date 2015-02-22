package gabrielssilva.podingcast.app;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import gabrielssilva.podingcast.adapter.EpisodesAdapter;
import gabrielssilva.podingcast.controller.ServiceController;
import gabrielssilva.podingcast.model.Episode;
import gabrielssilva.podingcast.model.Podcast;

public class EpisodesFragment extends Fragment implements ListView.OnItemClickListener {

    public final static String TAG = "FILES_FRAGMENT";

    private ServiceController serviceController;
    private View rootView;
    private HomeActivity activity;

    private ListView listView;
    private Podcast podcast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_episodes, container, false);

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
        Bundle args = this.getArguments();
        podcast = args.getParcelable(PodcastsFragment.ARG_PODCAST);
    }

    private void initListView() {
        Context context = activity.getApplicationContext();
        EpisodesAdapter adapter = new EpisodesAdapter(context, this.podcast.getEpisodes());

        this.listView.setAdapter(adapter);
        this.listView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
        List<Episode> episodes = this.podcast.getEpisodes();
        this.serviceController.playFile(episodes.get(index));

        ViewPager viewPager = (ViewPager) this.activity.findViewById(R.id.view_pager);
        viewPager.setCurrentItem(HomeActivity.PLAYER_FRAGMENT_POS, true);
    }
}