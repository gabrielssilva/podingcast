package gabrielssilva.podingcast.web;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

import gabrielssilva.podingcast.app.interfaces.CallbackListener;
import gabrielssilva.podingcast.view.SmartImageView;

public class LoadImageTask extends AsyncTask<SmartImageView.Param, Void, Bitmap> {

    private CallbackListener callbackListener;
    private File cacheDir;

    public LoadImageTask(CallbackListener callbackListener, File cacheDir) {
        this.callbackListener = callbackListener;
        this.cacheDir = cacheDir;
    }


    @Override
    protected Bitmap doInBackground(SmartImageView.Param... params) {
        SmartImageView.Param param = params[0];
        Bitmap imageBitmap = restoreBitmap(param.key);

        if (imageBitmap == null) {
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) param.imageURL.openConnection();
                urlConnection.connect();
                imageBitmap = BitmapFactory.decodeStream(urlConnection.getInputStream());

                cacheBitmap(param.key, imageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return imageBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap image) {
        if (image != null) {
            this.callbackListener.onSuccess(image);
        } else {
            this.callbackListener.onFailure(null);
        }
    }

    private Bitmap restoreBitmap(String key) {
        File savedBitmap = new File(cacheDir, key);

        return BitmapFactory.decodeFile(savedBitmap.getPath());
    }

    private void cacheBitmap(String key, Bitmap bitmap) throws IOException {
        File cachedBitmap = new File(cacheDir, key);
        FileOutputStream outputStream = new FileOutputStream(cachedBitmap);

        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        outputStream.close();
    }
}
