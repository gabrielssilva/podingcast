package gabrielssilva.podingcast.app;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import gabrielssilva.podingcast.adapter.DrawerAdapter;
import gabrielssilva.podingcast.events.DrawerItemClick;

public class HomeActivity extends Activity implements DrawerListener {

    private DrawerLayout drawerLayout;
    private ListView drawerListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.initViews();
        this.initDrawerList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.drawerListView = (ListView) findViewById(R.id.navigation_drawer);
    }

    private void initDrawerList() {
        DrawerAdapter drawerAdapter = new DrawerAdapter(this);
        DrawerItemClick onDrawerItemClick = new DrawerItemClick(this);

        this.drawerListView.setAdapter(drawerAdapter);
        this.drawerListView.setOnItemClickListener(onDrawerItemClick);
    }

    @Override
    public void changeFragment(Fragment newFragment, String title, int index) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, newFragment).commit();

        this.updateDrawer(index);
        this.updateTitle(title);
    }

    private void updateDrawer(int index) {
        this.drawerListView.setItemChecked(index, true);
        this.drawerLayout.closeDrawer(this.drawerListView);
    }

    private void updateTitle(String title) {
        getActionBar().setTitle(title);
    }
}
