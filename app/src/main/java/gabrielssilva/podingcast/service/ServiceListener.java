package gabrielssilva.podingcast.service;

public interface ServiceListener {
    public void setBound(boolean bound);
    public void setService(PlayerService playerService);
    public PlayerService getService();
}