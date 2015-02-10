package gabrielssilva.podingcast.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ContainerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_container, container, false);
        this.addFragmentToContainer();

        return rootView;
    }


    private void addFragmentToContainer() {
        FragmentTransaction transaction = this.getFragmentManager().beginTransaction();

        transaction.replace(R.id.container, new FeedFragment());
        transaction.commit();
    }
}
