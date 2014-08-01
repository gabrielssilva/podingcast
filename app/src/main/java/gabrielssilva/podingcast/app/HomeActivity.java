package gabrielssilva.podingcast.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import gabrielssilva.podingcast.adapter.SectionsPagerAdapter;
import gabrielssilva.podingcast.app.R;

public class HomeActivity extends FragmentActivity implements ActionBar.TabListener {

    private SectionsPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        actionBar = getActionBar();
        pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(pagerAdapter);
        this.setViewPagerListener();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        this.setTabsNames();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    private void setTabsNames() {
        int tabsCount = pagerAdapter.getCount();
        String[] tabsNames = getResources().getStringArray(R.array.tabs);

        for (int i=0; i<tabsCount; i++) {
            actionBar.addTab(actionBar.newTab().setText(tabsNames[i]).setTabListener(this));
        }
    }

    // The class is declared here to avoid the declaration on function call.
    private class ViewPagerListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // Do nothing.
        }

        @Override
        public void onPageSelected(int position) {
            actionBar.setSelectedNavigationItem(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // Do nothing.
        }
    }

    private void setViewPagerListener() {
        ViewPagerListener listener = new ViewPagerListener();
        viewPager.setOnPageChangeListener(listener);
    }
}
