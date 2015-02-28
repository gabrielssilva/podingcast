package gabrielssilva.podingcast.app;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import gabrielssilva.podingcast.adapter.PodcastsAdapter;
import gabrielssilva.podingcast.controller.FilesController;
import gabrielssilva.podingcast.model.Podcast;
import gabrielssilva.podingcast.view.Animator;

public class PodcastsFragment extends Fragment implements ListView.OnItemClickListener,
        ViewTreeObserver.OnGlobalLayoutListener{

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

        ViewTreeObserver viewTree = this.listView.getViewTreeObserver();
        viewTree.addOnGlobalLayoutListener(this);
    }

    private void loadPodcasts() {
        FilesController filesController = new FilesController(getActivity());

        this.podcasts = filesController.getAllPodcasts();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int index, long id) {
        Animator animator = new Animator();
        animator.fadeListOut(this.listView, new MyAnimatorListener(index), index);
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


    private class MyAnimatorListener implements android.animation.Animator.AnimatorListener {

        private int index;

        public MyAnimatorListener(int index) {
            this.index = index;
        }


        @Override
        public void onAnimationStart(android.animation.Animator animator) {
            // Stop handling click events
            listView.setOnItemClickListener(null);
        }

        @Override
        public void onAnimationEnd(android.animation.Animator animator) {
            Fragment filesFragment = new EpisodesFragment();
            Bundle args = new Bundle();

            args.putParcelable(ARG_PODCAST, podcasts.get(this.index));
            filesFragment.setArguments(args);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.container, filesFragment, EpisodesFragment.TAG);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        @Override
        public void onAnimationCancel(android.animation.Animator animator) {
            // Do nothing
        }

        @Override
        public void onAnimationRepeat(android.animation.Animator animator) {
            // Do nothing
        }
    }
}