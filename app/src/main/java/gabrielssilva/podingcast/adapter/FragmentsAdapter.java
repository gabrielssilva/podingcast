package gabrielssilva.podingcast.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import gabrielssilva.podingcast.app.ContainerFragment;
import gabrielssilva.podingcast.app.PlayerFragment;


public class FragmentsAdapter extends FragmentPagerAdapter {

    private static final String[] TITLES = {"Feed", "Player"};

    public FragmentsAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return new ContainerFragment();
        } else {
            return new PlayerFragment();
        }
    }
}
