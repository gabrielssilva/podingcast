package gabrielssilva.podingcast.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import gabrielssilva.podingcast.controller.ServiceController;
import gabrielssilva.podingcast.database.FilesDbManager;

public class HomeActivity extends Activity implements ListSelectionListener {

    private ServiceController serviceController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.serviceController = new ServiceController(this);
        this.serviceController.initService();

        //FilesDbManager dbManager = new FilesDbManager(getApplicationContext());
        //dbManager.refreshDatabase();

        this.openFragment(new FeedFragment());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.serviceController.saveCurrentPosition();
        this.serviceController.destroyService();
    }


    public void openFragment(Fragment newFragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, newFragment).commit();
    }

    public void onFeedSelected(String feedName) {
        Fragment filesFragment = new FilesFragment();
        Bundle args = new Bundle();

        args.putString(FeedFragment.ARG_FEED_NAME, feedName);
        filesFragment.setArguments(args);

        this.openFragment(filesFragment);
    }

    public void onFileSelected(String fileName) {
        Fragment playerFragment = new PlayerFragment();

        this.serviceController.playFile(fileName);
        this.openFragment(playerFragment);
    }

    public ServiceController getServiceController() {
        return this.serviceController;
    }
}