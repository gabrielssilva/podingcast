package gabrielssilva.podingcast.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.net.MalformedURLException;
import java.net.URL;

import gabrielssilva.podingcast.app.R;
import gabrielssilva.podingcast.app.interfaces.CallbackListener;
import gabrielssilva.podingcast.web.LoadImageTask;

public class SmartImageView extends ImageView implements CallbackListener {

    private LoadImageTask loadImageTask;
    private String oldSource;

    public SmartImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Bitmap defaultCover = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.default_feed_cover);
        this.setImageBitmap(defaultCover);

        this.setOldSource(null);
        this.loadImageTask = null;
    }

    public void setSource(String key, String imageAddress) {
        genericSetSource(key, imageAddress, true, this);
    }


    protected void genericSetSource(String key, String imageAddress, boolean saveOnCache,
                                  CallbackListener callbackListener) {

        // Only loads the image if the requested source is different from the current.
        if (!imageAddress.equals(this.oldSource)) {
            this.cancelOldDownload();

            try {
                Param param = new Param(key, new URL(imageAddress), saveOnCache);
                this.loadImageTask = new LoadImageTask(callbackListener,
                        this.getContext().getCacheDir());
                loadImageTask.execute(param);

                this.setOldSource(imageAddress);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    protected void setOldSource(String oldSource) {
        this.oldSource = oldSource;
    }

    private void cancelOldDownload() {
        if (this.loadImageTask != null) {
            this.loadImageTask.cancel(true);
        }
    }

    @Override
    public void onSuccess(Object result) {
        Bitmap resultBitmap = (Bitmap) result;
        this.setImageBitmap(resultBitmap);
    }

    @Override
    public void onFailure(Object result) {
        this.setOldSource(null);
    }


    public class Param {
        public String key;
        public URL imageURL;
        public boolean saveOnCache;

        public Param(String key, URL imageURL, boolean saveOnCache) {
            this.key = key;
            this.imageURL = imageURL;
            this.saveOnCache = saveOnCache;
        }
    }
}
