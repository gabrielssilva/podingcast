package gabrielssilva.podingcast.app;

import android.content.res.Resources;

import gabrielssilva.podingcast.service.PlayerService;

public interface PlayerListener extends EventListener {
    public Resources getResources();
    public PlayerService getService();
}
