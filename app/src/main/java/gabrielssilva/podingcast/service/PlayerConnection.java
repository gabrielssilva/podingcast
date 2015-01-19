package gabrielssilva.podingcast.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import gabrielssilva.podingcast.service.PlayerService.PlayerBinder;

public class PlayerConnection implements ServiceConnection {

    private ServiceListener serviceListener;

    public PlayerConnection(ServiceListener serviceListener) {
        this.serviceListener = serviceListener;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        PlayerBinder binder = (PlayerBinder) service;
        PlayerService playerService;

        playerService = binder.getService();
        playerService.loadAudio("");

        this.serviceListener.setService(playerService);
        this.serviceListener.setBound(true);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        this.serviceListener.setBound(false);
    }
}
