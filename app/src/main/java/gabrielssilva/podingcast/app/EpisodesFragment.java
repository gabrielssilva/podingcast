package gabrielssilva.podingcast.app;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import gabrielssilva.podingcast.adapter.EpisodesAdapter;
import gabrielssilva.podingcast.app.interfaces.CallbackListener;
import gabrielssilva.podingcast.controller.EpisodesController;
import gabrielssilva.podingcast.controller.LocalFilesController;
import gabrielssilva.podingcast.controller.PodcastController;
import gabrielssilva.podingcast.controller.ServiceController;
import gabrielssilva.podingcast.database.FilesDbHelper;
import gabrielssilva.podingcast.model.Episode;
import gabrielssilva.podingcast.model.Podcast;
import gabrielssilva.podingcast.view.Animator;

public class EpisodesFragment extends Fragment implements ListView.OnItemClickListener,
        ViewTreeObserver.OnGlobalLayoutListener {

    public final static String TAG = "FILES_FRAGMENT";
    private final static int NUM_EPISODES = 5;

    private ServiceController serviceController;
    private View rootView;
    private HomeActivity activity;

    private EpisodesAdapter adapter;
    private ListView listView;
    private TextView titleView;
    private ProgressBar progressBar;
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
        this.titleView = (TextView) this.rootView.findViewById(R.id.episodes_page_title);
        this.listView = (ListView) this.rootView.findViewById(R.id.list_view);
        this.progressBar = (ProgressBar) this.rootView.findViewById(R.id.episodes_page_progress);
    }

    private void retrieveInfo() {
        Bundle args = this.getArguments();
        this.podcast = args.getParcelable(PodcastsFragment.ARG_PODCAST);
        this.titleView.setText(podcast.getPodcastName());

        fetchPodcast();
    }

    private void fetchPodcast() {
        PodcastController podcastController = new PodcastController(new FetchEpisodesListener());
        podcastController.fetchPodcast(this.podcast.getRssAddress(), NUM_EPISODES);
    }

    private void initListView() {
        Context context = activity.getApplicationContext();
        this.adapter = new EpisodesAdapter(context, this.podcast);

        this.listView.setAdapter(adapter);
        this.listView.setOnItemClickListener(this);

        ViewTreeObserver viewTree = this.listView.getViewTreeObserver();
        viewTree.addOnGlobalLayoutListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
        List<Episode> episodes = this.podcast.getEpisodes();
        Episode selectedEpisode = episodes.get(index);

        if (selectedEpisode.isLocal()) {
            this.serviceController.playFile(selectedEpisode);

            ViewPager viewPager = (ViewPager) this.activity.findViewById(R.id.view_pager);
            viewPager.setCurrentItem(HomeActivity.PLAYER_FRAGMENT_POS, true);
        } else {
            EpisodesController episodesController =
                    new EpisodesController(new DownloadListener(), this.activity);
            episodesController.downloadEpisode(selectedEpisode);
        }
    }

    /*
     * Will be called when the ListView draws the list item
     * We remove the listener to avoid calling it repeatedly
     */
    @Override
    public void onGlobalLayout() {
        ViewTreeObserver viewTree = this.listView.getViewTreeObserver();
        viewTree.removeOnGlobalLayoutListener(this);

        Animator animator = new Animator();
        animator.fadeListIn(this.listView, null, 0);
    }

    private class FetchEpisodesListener implements CallbackListener {

        @Override
        public void onSuccess(Object result) {
            Podcast fetchedPodcast = (Podcast) result;

            EpisodesController episodesController =
                    new EpisodesController(new DownloadListener(), activity);
            List<Episode> updatedEpisodes = episodesController.compareEpisodes(podcast.getEpisodes(),
                    fetchedPodcast.getEpisodes());

            podcast.setEpisodes(updatedEpisodes);
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onFailure() {
            Toast.makeText(activity, "Couldn't load episodes", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    }

    private class DownloadListener implements CallbackListener {

        @Override
        public void onSuccess(Object result) {
            FilesDbHelper filesDbHelper = new FilesDbHelper(activity.getApplicationContext());
            filesDbHelper.insertEpisode(podcast.getPodcastName(), (Episode) result);

            LocalFilesController localFilesController =
                    new LocalFilesController(activity.getApplicationContext());
            localFilesController.updatePodcast(podcast);

            fetchPodcast();
            Log.i("EpisodesFragment", ((Episode) result).getFilePath());
        }

        @Override
        public void onFailure() {
            Log.i("EpisodesFragment", "Failed");
        }
    }
}