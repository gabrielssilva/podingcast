package gabrielssilva.podingcast.web;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;

import gabrielssilva.podingcast.app.interfaces.CallbackListener;

public class DownloadFeedTask extends AsyncTask<String, Void, InputStream> {

    private CallbackListener callbackListener;

    public DownloadFeedTask(CallbackListener callbackListener) {
        this.callbackListener = callbackListener;
    }

    @Override
    protected InputStream doInBackground(String... params) {
        InputStream xmlFileStream = null;

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(params[0]);

        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();

            xmlFileStream = httpEntity.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return xmlFileStream;
    }

    @Override
    protected void onPostExecute(InputStream xmlFileStream) {
        if (xmlFileStream != null) {
            callbackListener.onSuccess(xmlFileStream);
        } else {
            callbackListener.onFailure();
        }
    }
}
