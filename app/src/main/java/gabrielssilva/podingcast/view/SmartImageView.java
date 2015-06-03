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

    public SmartImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Bitmap defaultCover = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.default_feed_cover);
        this.setImageBitmap(defaultCover);
    }

    public void setSource(String key, String imageAddress) {
        try {
            Param param = new Param(key, new URL(imageAddress));
            LoadImageTask loadImageTask = new LoadImageTask(this, this.getContext().getCacheDir());
            loadImageTask.execute(param);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onSuccess(Object result) {
        Bitmap image = (Bitmap) result;
        this.setImageBitmap(image);

        // Cache image...
    }

    @Override
    public void onFailure(Object result) {

    }

    public class Param {
        public String key;
        public URL imageURL;

        public Param(String key, URL imageURL) {
            this.key = key;
            this.imageURL = imageURL;
        }
    }
}
