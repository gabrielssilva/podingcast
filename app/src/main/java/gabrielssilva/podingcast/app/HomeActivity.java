package gabrielssilva.podingcast.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import gabrielssilva.podingcast.adapter.FragmentsAdapter;
import gabrielssilva.podingcast.app.interfaces.CallbackListener;
import gabrielssilva.podingcast.controller.ServiceController;
import gabrielssilva.podingcast.database.FilesDbHelper;
import gabrielssilva.podingcast.database.FilesDbManager;
import gabrielssilva.podingcast.model.Podcast;

public class HomeActivity extends FragmentActivity implements CallbackListener {

    public final static int PLAYER_FRAGMENT_POS = 1;

    private ServiceController serviceController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FilesDbManager dbManager = new FilesDbManager(this);
        dbManager.refreshDatabase();

        FragmentsAdapter adapter = new FragmentsAdapter(this.getSupportFragmentManager());

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Fragment filesFragment = getSupportFragmentManager().findFragmentByTag(EpisodesFragment.TAG);
        /*
         * If FeedList is visible, the back button will just send the App to background
         * (without calling onDestroy()). If FilesList is displayed, it will get back one screen.
         */
        if (filesFragment == null) {
            moveTaskToBack(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSuccess(Object result) {
        FilesDbHelper dbHelper = new FilesDbHelper(this);
        dbHelper.insertPodcast((Podcast) result);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(PodcastsFragment.TAG);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.detach(fragment);
        transaction.attach(fragment);
        transaction.commit();

        Toast.makeText(this, "Feed downloaded", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure() {
        Toast.makeText(this, "Feed not downloaded", Toast.LENGTH_LONG).show();
    }


    /*
     * The following two methods are used by the fragments to exchange the ServiceController.
     * The PlayerFragment will set it after creating a new ServiceController.
     * The FilesFragment will get it when necessary.
     */
    public void setServiceController(ServiceController serviceController) {
        this.serviceController = serviceController;
    }

    public ServiceController getServiceController() {
        return this.serviceController;
    }
}