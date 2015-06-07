package gabrielssilva.podingcast.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.SparseArray;

import gabrielssilva.podingcast.app.interfaces.CallbackListener;

public class CachedImageView extends SmartImageView {

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
            this.bitmaps.append(this.index, resultBitmap);
            setImageBitmap(resultBitmap);
        }

        @Override
        public void onFailure(Object result) {
            setOldSource(null);
        }
    }
}
