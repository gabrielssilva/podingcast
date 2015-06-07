package gabrielssilva.podingcast.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import gabrielssilva.podingcast.adapter.FragmentsAdapter;
import gabrielssilva.podingcast.controller.PlayerController;

public class HomeActivity extends FragmentActivity {

    public final static int PLAYER_FRAGMENT_POS = 1;

    private PlayerController playerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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


    /*
     * The following two methods are used by the fragments to exchange the ServiceController.
     * The PlayerFragment will set it after creating a new ServiceController.
     * The FilesFragment will get it when necessary.
     */
    public void setPlayerController(PlayerController playerController) {
        this.playerController = playerController;
    }

    public PlayerController getPlayerController() {
        return this.playerController;
    }
}