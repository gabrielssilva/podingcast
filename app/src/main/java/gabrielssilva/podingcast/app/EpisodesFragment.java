package gabrielssilva.podingcast.app;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.LongSparseArray;
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
import gabrielssilva.podingcast.controller.PlayerController;
import gabrielssilva.podingcast.helper.FilesHelper;
import gabrielssilva.podingcast.model.Episode;
import gabrielssilva.podingcast.model.Podcast;
import gabrielssilva.podingcast.service.BroadcastNotifier;
import gabrielssilva.podingcast.service.DownloadNotifier;
import gabrielssilva.podingcast.service.DownloadNotifyService;
import gabrielssilva.podingcast.view.Animator;

public class EpisodesFragment extends Fragment implements ListView.OnItemClickListener,
        ViewTreeObserver.OnGlobalLayoutListener {

    public final static String TAG = "FILES_FRAGMENT";
    private final static int NUM_EPISODES = 5;

    private HomeActivity activity;
    private PlayerController playerController;
    private Podcast podcast;
    private LongSparseArray<Episode> downloading;
    private BroadcastNotifier broadcastNotifier;

    private View rootView;
    private EpisodesAdapter adapter;
    private ListView listView;
    private TextView titleView;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Retrieve the episodes from somewhere
        this.downloading = new LongSparseArray<>();
        this.broadcastNotifier = null;

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_episodes, container, false);

        this.rootView = rootView;
        this.activity = (HomeActivity) getActivity();
        this.playerController = this.activity.getPlayerController();

        this.initViews(inflater);
        this.retrieveInfo();


        return rootView;
    }

    @Override
    public void onResume() {
        this.retrieveInfo();
        this.initListView();

        this.recreateDownloads();
        this.updateEpisodesStatus();

        if (this.downloading.size() > 0) {
            // More Downloads to watch
            registerLocalNotifier();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        this.downloading.clear();

        if (this.broadcastNotifier != null) {
            this.activity.unregisterReceiver(broadcastNotifier);
            this.broadcastNotifier = null;
        }
        super.onPause();
    }


    private void initViews(LayoutInflater inflater) {
        this.titleView = (TextView) this.rootView.findViewById(R.id.episodes_page_title);
        this.listView = (ListView) this.rootView.findViewById(R.id.list_view);
        this.progressBar = (ProgressBar) this.rootView.findViewById(R.id.episodes_page_progress);

        View footerView = inflater.inflate(R.layout.footer_episodes_list, listView, false);
        this.listView.addFooterView(footerView);
    }

    private void retrieveInfo() {
        Bundle args = this.getArguments();
        String podcastName = args.getString(PodcastsFragment.ARG_PODCAST);

        LocalFilesController localFilesController = new LocalFilesController(this.activity);
        this.podcast = localFilesController.getPodcast(podcastName);
        this.titleView.setText(podcast.getPodcastName());

        fetchPodcast();
    }

    private void fetchPodcast() {
        PodcastController podcastController = new PodcastController(new FetchEpisodesListener());
        podcastController.fetchPodcast(this.podcast.getRssAddress(), NUM_EPISODES);
    }

    private void recreateDownloads() {
        // Update the episodes status according to the saved information
        LongSparseArray<String> references = FilesHelper.getDownloadReferences(this.activity);

        for (int i=0; i<references.size(); i++ ) {
            for (Episode episode : this.podcast.getEpisodes()) {
                if (references.valueAt(i).equals(episode.getUrl())) {
                    episode.setStatus(Episode.DOWNLOADING);
                    this.downloading.append(references.keyAt(i), episode);
                    break;
                }
            }
        }
    }

    private void updateEpisodesStatus() {
        List<Episode> episodes = this.podcast.getEpisodes();

        for (int i=0; i<this.downloading.size(); i++) {
            for (Episode episode : episodes) {
                if (episode.getUrl().equals(this.downloading.valueAt(i).getUrl())) {
                    episode.setStatus(Episode.DOWNLOADING);
                }
            }
        }
    }

    private void initListView() {
        this.adapter = new EpisodesAdapter(this.activity, this.podcast);

        this.listView.setAdapter(adapter);
        this.listView.setOnItemClickListener(this);

        ViewTreeObserver viewTree = this.listView.getViewTreeObserver();
        viewTree.addOnGlobalLayoutListener(this);
    }

    private void registerLocalNotifier() {
        // If any broadcast receiver was registered, register a new one
        if (this.broadcastNotifier == null) {
            this.broadcastNotifier = new BroadcastNotifier(new DownloadListener(),
                    DownloadNotifier.ACTION_DOWNLOAD_OK);
            IntentFilter intentFilter = new IntentFilter(DownloadNotifier.ACTION_DOWNLOAD_OK);
            intentFilter.addAction(DownloadNotifier.ACTION_DOWNLOAD_FAIL);
            this.activity.registerReceiver(this.broadcastNotifier, intentFilter);
        }
    }

    private void playEpisode(Episode episode) {
        this.playerController.playFile(episode);

        ViewPager viewPager = (ViewPager) this.activity.findViewById(R.id.view_pager);
        viewPager.setCurrentItem(HomeActivity.PLAYER_FRAGMENT_POS, true);
    }

    private void downloadEpisode(Episode episode) {
        episode.setStatus(Episode.DOWNLOADING);
        this.adapter.notifyDataSetChanged();

        EpisodesController episodesController = new EpisodesController(this.activity);
        long downloadID = episodesController.downloadEpisode(this.podcast, episode);

        FilesHelper.saveDownloadReference(this.activity, downloadID, episode.getUrl());
        this.waitForDownload(downloadID);
        this.downloading.append(downloadID, episode);
    }

    private void waitForDownload(long downloadID) {
        Intent intent = new Intent(this.activity, DownloadNotifyService.class);
        intent.putExtra(DownloadNotifyService.DOWNLOAD_ID, downloadID);

        this.activity.startService(intent);
        this.registerLocalNotifier();
    }

    private void selectEpisode(int index) {
        List<Episode> episodes = this.podcast.getEpisodes();
        Episode selectedEpisode = episodes.get(index);

        if (selectedEpisode.getStatus().equals(Episode.LOCAL)) {
            playEpisode(selectedEpisode);
        } else if (selectedEpisode.getStatus().equals(Episode.NOT_LOCAL)) {
            downloadEpisode(selectedEpisode);
        }
    }

    private void moreEpisodes() {
        int amountToFetch = this.podcast.getEpisodes().size() + NUM_EPISODES;
        this.progressBar.setVisibility(View.VISIBLE);

        PodcastController podcastController = new PodcastController(new FetchEpisodesListener());
        podcastController.fetchPodcast(this.podcast.getRssAddress(), amountToFetch);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
        if (index == this.podcast.getEpisodes().size()) {
            moreEpisodes();
        } else {
            selectEpisode(index);
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

            EpisodesController episodesController = new EpisodesController(activity);
            List<Episode> updatedEpisodes = episodesController.compareEpisodes(podcast.getEpisodes(),
                    fetchedPodcast.getEpisodes());

            podcast.setEpisodes(updatedEpisodes);
            recreateDownloads();
            updateEpisodesStatus();

            if (downloading.size() > 0) {
                // More Downloads to watch
                registerLocalNotifier();
            }

            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onFailure(Object result) {
            Toast.makeText(activity, "Couldn't load episodes", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    }

    private class DownloadListener implements CallbackListener {

        @Override
        public void onSuccess(Object result) {
            LocalFilesController localFilesController =
                    new LocalFilesController(activity.getApplicationContext());
            localFilesController.updatePodcast(podcast);

            fetchPodcast();

            this.updateEpisodeStatus((long) result, Episode.LOCAL);
        }

        @Override
        public void onFailure(Object result) {
            this.updateEpisodeStatus((long) result, Episode.NOT_LOCAL);
        }

        private void updateEpisodeStatus(long downloadID, String status) {

            Episode downloadedEpisode = downloading.get(downloadID);
            downloadedEpisode.setStatus(status);
            downloading.remove(downloadID);

            activity.unregisterReceiver(broadcastNotifier);
            broadcastNotifier = null;

            if (downloading.size() > 0) {
                // More Downloads to watch
                registerLocalNotifier();
            }

            adapter.notifyDataSetChanged();
        }
    }
}