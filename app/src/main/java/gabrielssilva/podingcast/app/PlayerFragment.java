package gabrielssilva.podingcast.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import gabrielssilva.podingcast.events.OnBackAudioPositionClick;
import gabrielssilva.podingcast.events.OnPlayPauseClick;
import gabrielssilva.podingcast.events.OnSkipAudioPositionClick;
import gabrielssilva.podingcast.service.Connection;
import gabrielssilva.podingcast.service.PlayerConnection;
import gabrielssilva.podingcast.service.PlayerService;

public class PlayerFragment extends Fragment implements Connection, PlayerListener {

    private PlayerService playerService;
    private Intent playerIntent;
    private boolean bound = false;
    private ServiceConnection playerConnection;
    private Activity activity;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);

        this.playerConnection = new PlayerConnection(this);
        this.activity = getActivity();
        this.rootView = rootView;
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
        this.setButtonEvents();
    }

    public void setButtonEvents() {
        Button buttonPlayPause = (Button) this.rootView.findViewById(R.id.button_play_pause);
        Button buttonSkipAudioPosition = (Button) this.rootView.findViewById(R.id.button_plus_30);
        Button buttonBackAudioPosition = (Button) this.rootView.findViewById(R.id.button_minus_30);

        OnPlayPauseClick playPauseEvent = new OnPlayPauseClick(this);
        buttonPlayPause.setOnClickListener(playPauseEvent);

        OnSkipAudioPositionClick skipAudioPositionClick = new OnSkipAudioPositionClick(this);
        buttonSkipAudioPosition.setOnClickListener(skipAudioPositionClick);

        OnBackAudioPositionClick backAudioPositionClick = new OnBackAudioPositionClick(this);
        buttonBackAudioPosition.setOnClickListener(backAudioPositionClick);
    }

    @Override
    public View getRootView() {
        return this.rootView;
    }
}
