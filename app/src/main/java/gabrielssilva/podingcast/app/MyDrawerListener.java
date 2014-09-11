package gabrielssilva.podingcast.app;

import android.app.Fragment;

public interface MyDrawerListener {
    public void changeFragment(Fragment newFragment, String title, int index);
}