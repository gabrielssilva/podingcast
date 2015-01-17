package gabrielssilva.podingcast.events;

import android.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import gabrielssilva.podingcast.app.FeedFragment;
import gabrielssilva.podingcast.app.MyDrawerListener;
import gabrielssilva.podingcast.app.PlayerFragment;

public class DrawerItemClick implements ListView.OnItemClickListener {

    private MyDrawerListener drawerListener;

    public DrawerItemClick(MyDrawerListener drawerListener) {
        this.drawerListener = drawerListener;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
        Fragment newFragment = createFragment(index);
        String newTitle = "Title";

        this.drawerListener.changeFragment(newFragment, newTitle, index);
    }

    private Fragment createFragment(int index) {
        Fragment newFragment = null;

        switch (index) {
            case 0:
                newFragment = new FeedFragment();
                break;
            case 1:
                newFragment = new PlayerFragment();
        }

        return newFragment;
    }
}