package gabrielssilva.podingcast.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import gabrielssilva.podingcast.controller.ServiceController;
import gabrielssilva.podingcast.service.PlayerService.PlayerBinder;

public class PlayerConnection implements ServiceConnection {

    private ServiceController serviceController;

    public PlayerConnection(ServiceController serviceController) {
        this.serviceController = serviceController;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        PlayerBinder binder = (PlayerBinder) service;
        PlayerService playerService;

        playerService = binder.getService();
        playerService.loadAudio("");

        this.serviceController.setService(playerService);
        this.serviceController.setBound(true);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        this.serviceController.setBound(false);
    }
}
