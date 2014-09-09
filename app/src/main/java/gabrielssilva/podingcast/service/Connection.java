package gabrielssilva.podingcast.service;

public interface Connection {
    public void setBound(boolean bound);
    public PlayerService getService();
    public void setService(PlayerService playerService);
    public void initSeekBar();
}
