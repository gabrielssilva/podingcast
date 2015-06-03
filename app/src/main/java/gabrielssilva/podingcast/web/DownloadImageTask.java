package gabrielssilva.podingcast.web;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import gabrielssilva.podingcast.app.interfaces.CallbackListener;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private CallbackListener callbackListener;

    public DownloadImageTask(CallbackListener callbackListener) {
        this.callbackListener = callbackListener;
    }


    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap imageBitmap = null;

        try {
            URL imageURL = new URL(params[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) imageURL.openConnection();

            urlConnection.connect();
            imageBitmap = BitmapFactory.decodeStream(urlConnection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
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
}
