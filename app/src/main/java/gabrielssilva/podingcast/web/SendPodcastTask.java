package gabrielssilva.podingcast.web;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

public class SendPodcastTask extends AsyncTask<Object, Void, String> {

    protected String doInBackground(Object... params) {

        final String path = "http://10.10.10.104:8080/save_podcast";
        StringEntity data = (StringEntity) params[0];

        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(path);
            httpPost.setEntity(data);
            httpPost.setHeader("Accept", "text/plain");
            httpPost.setHeader("Content-type", "application/json");

            ResponseHandler responseHandler = new BasicResponseHandler();
            return (String) httpClient.execute(httpPost, responseHandler);

        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

    protected void onPostExecute(String response) {
        if (response != null) {
            Log.i("Web Send", response);
        }
    }

}
