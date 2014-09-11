package gabrielssilva.podingcast.app;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import gabrielssilva.podingcast.adapter.DrawerAdapter;
import gabrielssilva.podingcast.events.DrawerItemClick;
import gabrielssilva.podingcast.service.Connection;
import gabrielssilva.podingcast.service.PlayerConnection;
import gabrielssilva.podingcast.service.PlayerService;

public class HomeActivity extends Activity implements Connection, MyDrawerListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView drawerListView;

    private ServiceConnection playerConnection;
    private PlayerService playerService;
    private Intent playerIntent;
    private boolean bound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.playerConnection = new PlayerConnection(this);

        if (playerIntent == null) {
            Log.i("Player Fragment", "Creating a new intent");
            playerIntent = new Intent(this, PlayerService.class);
            bindService(playerIntent, playerConnection, Context.BIND_AUTO_CREATE);
            startService(playerIntent);
        }

        this.initViews();
        this.initDrawerLayout();
        this.initDrawerList();
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
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
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
        getActionBar().setTitle(title);
    }

    @Override
    public void setBound(boolean bound) {
        this.bound = bound;
    }

    public PlayerService getService() {
        return this.playerService;
    }

    @Override
    public void setService(PlayerService playerService) {
        this.playerService = playerService;
    }
}
