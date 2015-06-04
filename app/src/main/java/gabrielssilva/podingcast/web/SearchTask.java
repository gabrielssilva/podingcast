package gabrielssilva.podingcast.web;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import gabrielssilva.podingcast.app.interfaces.CallbackListener;

public class SearchTask extends AsyncTask<String, Void, JSONObject> {

    private final static String prefix = "https://itunes.apple.com/search?term=";
    private final static String suffix = "&entity=podcast";

    private CallbackListener callbackListener;

    public SearchTask(CallbackListener callbackListener) {
        this.callbackListener = callbackListener;
    }


    @Override
    protected JSONObject doInBackground(String... strings) {
        JSONObject jsonResult = null;

        try {
            URL url = new URL(prefix + strings[0] + suffix);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            jsonResult = new JSONObject(getStringResult(urlConnection.getInputStream()));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return jsonResult;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        if (result != null) {
            this.callbackListener.onSuccess(result);
        } else {
            this.callbackListener.onFailure(null);
        }
    }


    private String getStringResult(InputStream inputStream) throws IOException {
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuilder strBuilder = new StringBuilder();

        String line = bufferedReader.readLine();
        while (line != null) {
            strBuilder.append(line);
            line = bufferedReader.readLine();
        }

        return strBuilder.toString();
    }
}
