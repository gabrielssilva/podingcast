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

    private boolean sourceSet;

    public SmartImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Bitmap defaultCover = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.default_feed_cover);
        this.setImageBitmap(defaultCover);
        this.sourceSet = false;
    }

    public void setSource(String key, String imageAddress) {
        genericSetSource(key, imageAddress, true);
    }

    public void setSource(String imageAddress) {
        genericSetSource(null, imageAddress, false);
    }


    private void genericSetSource(String key, String imageAddress, boolean saveOnCache) {
        if (!this.sourceSet) {
            try {
                Param param = new Param(key, new URL(imageAddress), saveOnCache);
                LoadImageTask loadImageTask = new LoadImageTask(this, this.getContext().getCacheDir());
                loadImageTask.execute(param);

                this.sourceSet = true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onSuccess(Object result) {
        Bitmap image = (Bitmap) result;
        this.setImageBitmap(image);
    }

    @Override
    public void onFailure(Object result) {
        this.sourceSet = false;
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
