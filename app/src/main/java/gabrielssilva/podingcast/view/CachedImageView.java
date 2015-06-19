package gabrielssilva.podingcast.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.SparseArray;

import gabrielssilva.podingcast.app.interfaces.CallbackListener;

public class CachedImageView extends SmartImageView {

    private final static int MAX_CACHE_SIZE = 10;

    public CachedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCachedSource(String imageAddress, int index, SparseArray<Bitmap> bitmaps) {
        Bitmap bitmap = bitmaps.get(index);

        if (bitmap == null) {
            this.genericSetSource(null, imageAddress, false,
                    new IndexedCallbackListener(index, bitmaps));
        } else {
            this.setImageBitmap(bitmap);
        }
    }

    public class IndexedCallbackListener implements CallbackListener {
        private int index;
        private SparseArray<Bitmap> bitmaps;

        public IndexedCallbackListener(int index, SparseArray<Bitmap> bitmaps) {
            this.index = index;
            this.bitmaps = bitmaps;
        }

        @Override
        public void onSuccess(Object result) {
            Bitmap resultBitmap = (Bitmap) result;
            setImageBitmap(resultBitmap);

            // Erase an old image, do not cache too much!
            int cacheSize = this.bitmaps.size();
            if (cacheSize > MAX_CACHE_SIZE) {
                this.bitmaps.remove(this.bitmaps.keyAt(0));
            }

            this.bitmaps.append(this.index, resultBitmap);
        }

        @Override
        public void onFailure(Object result) {
            setOldSource(null);
        }
    }
}
