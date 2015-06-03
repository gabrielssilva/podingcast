package gabrielssilva.podingcast.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.ImageView;

import gabrielssilva.podingcast.app.R;
import gabrielssilva.podingcast.app.interfaces.CallbackListener;
import gabrielssilva.podingcast.web.DownloadImageTask;

public class SmartImageView extends ImageView implements CallbackListener {

    public SmartImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Bitmap defaultCover = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.default_feed_cover);
        this.setImageBitmap(defaultCover);
    }

    public void setSource(String key, String imageAddress) {
        DownloadImageTask downloadImageTask = new DownloadImageTask(this);
        downloadImageTask.execute(imageAddress);
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
}
