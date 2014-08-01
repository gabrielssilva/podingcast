package gabrielssilva.podingcast.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import gabrielssilva.podingcast.app.FeedFragment;
import gabrielssilva.podingcast.app.PlayerFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final int NUMBER_OF_SECTIONS = 2;

    public SectionsPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new FeedFragment();
            case 1:
                return new PlayerFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return NUMBER_OF_SECTIONS;
    }
}
