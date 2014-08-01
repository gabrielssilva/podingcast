package gabrielssilva.podingcast.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import gabrielssilva.podingcast.service.PlayerService.PlayerBinder;

public class PlayerConnection implements ServiceConnection {

    private Connection connection;

    public PlayerConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        PlayerBinder binder = (PlayerBinder) service;
        PlayerService playerService = connection.getService();

        playerService = binder.getService();
        //playerService.setList(songList);
        this.connection.setService(playerService);

        this.connection.setBound(true);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        this.connection.setBound(false);
    }
}
