package gabrielssilva.podingcast.app.interfaces;

import android.content.Context;
import android.graphics.Bitmap;

public interface ServiceListener {
    public Context getApplicationContext();
    public void setSeekBar();
    public void setEpisodeCover(Bitmap bitmapCover);
}