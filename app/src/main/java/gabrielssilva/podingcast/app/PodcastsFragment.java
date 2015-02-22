package gabrielssilva.podingcast.app;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import gabrielssilva.podingcast.adapter.PodcastsAdapter;
import gabrielssilva.podingcast.controller.FilesController;
import gabrielssilva.podingcast.model.Podcast;

public class PodcastsFragment extends Fragment implements ListView.OnItemClickListener  {

    public final static String ARG_PODCAST = "PODCAST";
    public final static String TAG = "FEED_FRAGMENT";

    private ListView listView;
    private View rootView;
    private List<Podcast> podcasts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_podcasts, container, false);
        this.rootView = rootView;

        this.initViews();
        this.loadPodcasts();
        this.initListView();

        return rootView;
    }


    private void initViews() {
        this.listView = (ListView) this.rootView.findViewById(R.id.list_view);
    }

    private void initListView() {
        PodcastsAdapter adapter = new PodcastsAdapter(getActivity(), this.podcasts);

        this.listView.setAdapter(adapter);
        this.listView.setOnItemClickListener(this);
    }

    private void loadPodcasts() {
        FilesController filesController = new FilesController(getActivity());

        this.podcasts = filesController.getAllPodcasts();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
        Fragment filesFragment = new EpisodesFragment();
        Bundle args = new Bundle();

        args.putParcelable(ARG_PODCAST, this.podcasts.get(index));
        filesFragment.setArguments(args);

        FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, filesFragment, EpisodesFragment.TAG);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}