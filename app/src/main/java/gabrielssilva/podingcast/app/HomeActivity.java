package gabrielssilva.podingcast.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.ListView;

import gabrielssilva.podingcast.adapter.DrawerAdapter;
import gabrielssilva.podingcast.controller.ServiceController;
import gabrielssilva.podingcast.database.FilesDbManager;
import gabrielssilva.podingcast.events.DrawerItemClick;

public class HomeActivity extends Activity implements MyDrawerListener, ListSelectionListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView drawerListView;

    private ServiceController serviceController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.initViews();
        this.initDrawerLayout();
        this.initDrawerList();

        this.serviceController = new ServiceController(this);
        this.serviceController.initService();

        FilesDbManager dbManager = new FilesDbManager(getApplicationContext());
        dbManager.refreshDatabase();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        // Will do the icon transition tricks.
        super.onPostCreate(savedInstanceState);
        this.drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // To keep the configuration consistent.
        super.onConfigurationChanged(newConfig);
        this.drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // We don't have options menu, so the drawer will always handle the input.
        return this.drawerToggle.onOptionsItemSelected(item);
    }


    private void initViews() {
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.drawerListView = (ListView) findViewById(R.id.navigation_drawer);
    }

    private void initDrawerLayout() {
        drawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout, R.drawable.ic_drawer,
                R.string.drawer_opened, R.string.drawer_closed);

        this.drawerLayout.setDrawerListener(drawerToggle);

        if(getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
    }

    private void initDrawerList() {
        DrawerAdapter drawerAdapter = new DrawerAdapter(this);
        DrawerItemClick onDrawerItemClick = new DrawerItemClick(this);

        this.drawerListView.setAdapter(drawerAdapter);
        this.drawerListView.setOnItemClickListener(onDrawerItemClick);
    }

    @Override
    public void changeFragment(Fragment newFragment, String title, int index) {
        this.openFragment(newFragment);

        this.updateDrawer(index);
        this.updateTitle(title);
    }

    public void openFragment(Fragment newFragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, newFragment).commit();
    }

    private void updateDrawer(int index) {
        this.drawerListView.setItemChecked(index, true);
        this.drawerLayout.closeDrawer(this.drawerListView);
    }

    private void updateTitle(String title) {
        if (getActionBar() != null) {
            getActionBar().setTitle(title);
        }
    }

    public void onFeedSelected(String feedName) {
        Fragment filesFragment = new FilesFragment();
        Bundle args = new Bundle();

        args.putString(FeedFragment.ARG_FEED_NAME, feedName);
        filesFragment.setArguments(args);

        this.changeFragment(filesFragment, "Files", 0);
    }

    public void onFileSelected(String fileName) {
        Fragment playerFragment = new PlayerFragment();

        this.serviceController.playFile(fileName);
        this.changeFragment(playerFragment, "Player", 0);
    }

    public ServiceController getServiceController() {
        return this.serviceController;
    }
}