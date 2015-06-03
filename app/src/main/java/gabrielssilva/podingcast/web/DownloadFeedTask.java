package gabrielssilva.podingcast.web;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import gabrielssilva.podingcast.app.interfaces.CallbackListener;
import gabrielssilva.podingcast.controller.PodcastController;
import gabrielssilva.podingcast.parser.SaxHandler;

public class DownloadFeedTask extends AsyncTask<PodcastController.Params, Void, JSONObject> {

    private CallbackListener callbackListener;

    public DownloadFeedTask(CallbackListener callbackListener) {
        this.callbackListener = callbackListener;
    }

    @Override
    protected JSONObject doInBackground(PodcastController.Params... params) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(params[0].url);
        SaxHandler saxHandler = new SaxHandler(params[0].maxItems, httpGet);

        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream xmlFileStream = httpEntity.getContent();

            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            xmlReader.setContentHandler(saxHandler);
            xmlReader.parse(new InputSource(xmlFileStream));
        } catch (IOException | ParserConfigurationException e) {
            Log.e("DownloadFeedTask", "Error downloading the feed file");
            e.printStackTrace();
        } catch (SAXException e) {
            if (e instanceof SaxHandler.AllItemsParsedSaxException) {
                Log.i("JSON", saxHandler.getJson().toString());
                Log.i("DownloadFeedTask", "All items parsed");
            } else {
                Log.e("DownloadFeedTask", "Error parsing the XML file");
                e.printStackTrace();
            }
        }

        JSONObject podcastJson = saxHandler.getJson();
        if (podcastJson != null) {
            try {
                podcastJson.put("podingcast:link", params[0].url);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return podcastJson;
    }

    @Override
    protected void onPostExecute(JSONObject jsonPodcast) {
        if (jsonPodcast != null) {
            callbackListener.onSuccess(jsonPodcast);
        } else {
            callbackListener.onFailure(null);
        }
    }
}