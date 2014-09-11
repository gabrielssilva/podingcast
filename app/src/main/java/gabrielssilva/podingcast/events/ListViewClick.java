package gabrielssilva.podingcast.events;

import android.app.Fragment;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import gabrielssilva.podingcast.app.EventListener;
import gabrielssilva.podingcast.app.HomeActivity;
import gabrielssilva.podingcast.app.PlayerFragment;

public class ListViewClick implements ListView.OnItemClickListener {

    private HomeActivity homeActivity;

    public ListViewClick(EventListener eventListener) {
        this.homeActivity = (HomeActivity) eventListener.getActivity();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
        Fragment newFragment = new PlayerFragment();
        this.homeActivity.openFragment(newFragment);
    }
}
