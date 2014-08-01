package gabrielssilva.podingcast.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import gabrielssilva.podingcast.service.Connection;
import gabrielssilva.podingcast.service.PlayerConnection;
import gabrielssilva.podingcast.service.PlayerService;

public class PlayerFragment extends Fragment implements Connection {

    private PlayerService playerService;
    private Intent playerIntent;
    private boolean bound = false;
    private ServiceConnection playerConnection;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);

        this.playerConnection = new PlayerConnection(this);
        this.activity = getActivity();

        this.setButtonEvents(rootView);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (playerIntent == null) {
            playerIntent = new Intent(this.activity, PlayerService.class);
            this.activity.bindService(playerIntent, playerConnection, Context.BIND_AUTO_CREATE);
            this.activity.startService(playerIntent);
        }
    }

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

    public void setButtonEvents(View view) {
        Button button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerService.playAudio();
            }
        });

        Button button2 = (Button) view.findViewById(R.id.button_stop);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerService.pauseAudio();
            }
        });

    }
}
