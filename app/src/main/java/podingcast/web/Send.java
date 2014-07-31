package podingcast.web;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

public class Send extends AsyncTask<Object, Void, String> {

    protected String doInBackground(Object... params) {

        final String path = "http://10.10.10.104/save_podcast";
        StringEntity data = (StringEntity) params[0];

        Log.i("Web Send", "entered");

        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httPost = new HttpPost(path);
            Log.i("Web Send", "entered 1");
            httPost.setEntity(data);
            httPost.setHeader("Accept", "text/plain");
            httPost.setHeader("Content-type", "application/json");
            Log.i("Web Send", "entered 2");
            ResponseHandler responseHandler = new BasicResponseHandler();
            Log.i("Web Send", "entered 3");
            HttpResponse response = (HttpResponse) httpClient.execute(httPost, responseHandler);

            Log.i("Web Send", "exited");

            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();

            Log.i("Web Send", "exited");

            return null;
        }
    }

    protected void onPostExecute(String response) {
        Log.i("Web Send", response);
    }

}
