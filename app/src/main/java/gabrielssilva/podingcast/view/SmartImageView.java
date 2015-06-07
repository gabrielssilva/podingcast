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

    private String oldSource;

    public SmartImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Bitmap defaultCover = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.default_feed_cover);
        this.setImageBitmap(defaultCover);
        this.oldSource = null;
    }

    public void setSource(String key, String imageAddress) {
        genericSetSource(key, imageAddress, true);
    }

    public void setSource(String imageAddress) {
        genericSetSource(null, imageAddress, false);
    }


    private void genericSetSource(String key, String imageAddress, boolean saveOnCache) {
        // Only loads the image if the requested source is different from the current.
        if (!imageAddress.equals(this.oldSource)) {
            try {
                Param param = new Param(key, new URL(imageAddress), saveOnCache);
                LoadImageTask loadImageTask = new LoadImageTask(this, this.getContext().getCacheDir());
                loadImageTask.execute(param);

                this.oldSource = imageAddress;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSuccess(Object result) {
        //Bitmap currentBitmap = (Bitmap) this.getTag();

        //if (currentBitmap == null) {
            Bitmap image = (Bitmap) result;
            this.setImageBitmap(image);
            this.setTag(image);
        //} else {
        //    this.setImageBitmap(currentBitmap);
        //}
    }

    @Override
    public void onFailure(Object result) {
        this.oldSource = null;
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
