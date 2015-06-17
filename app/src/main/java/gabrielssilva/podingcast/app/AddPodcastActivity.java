package gabrielssilva.podingcast.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gabrielssilva.podingcast.adapter.SearchResultAdapter;
import gabrielssilva.podingcast.app.interfaces.CallbackListener;
import gabrielssilva.podingcast.database.FilesDbHelper;
import gabrielssilva.podingcast.model.Podcast;
import gabrielssilva.podingcast.web.SearchTask;

public class AddPodcastActivity extends Activity implements CallbackListener,
        AdapterView.OnItemClickListener {

    private ProgressBar progressBar;
    private SearchResultAdapter adapter;
    private List<Podcast> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_podcast);

        this.results = new ArrayList<>();
        this.adapter = new SearchResultAdapter(this, this.results);

        this.initViews();
    }


    private void initViews() {
        this.progressBar = (ProgressBar) this.findViewById(R.id.progress_add_podcast);

        ListView resultsList = (ListView) this.findViewById(R.id.results_list);
        resultsList.setAdapter(this.adapter);
        resultsList.setOnItemClickListener(this);

        EditText editText = (EditText) this.findViewById(R.id.search_field_add_podcast);
        editText.setOnEditorActionListener(new SearchAction());
    }

    private void searchPodcast(String podcastName) {
        SearchTask searchTask = new SearchTask(this);
        searchTask.execute(podcastName);

        this.progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public void onSuccess(Object result) {
        this.results.clear();

        try {
            JSONArray jsonResults = ((JSONObject) result).getJSONArray("results");

            for (int i=0; i<jsonResults.length(); i++) {
                Podcast podcast = new Podcast();
                JSONObject currentResult = jsonResults.getJSONObject(i);

                podcast.setPodcastName(currentResult.getString("collectionName"));
                podcast.setImageAddress(currentResult.getString("artworkUrl600"));
                podcast.setRssAddress(currentResult.getString("feedUrl"));
                this.results.add(podcast);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.adapter.notifyDataSetChanged();
        this.progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFailure(Object result) {
        this.progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
        FilesDbHelper dbHelper = new FilesDbHelper(this);
        dbHelper.insertPodcast(this.results.get(index));

        this.onBackPressed();
    }


    private class SearchAction implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView textView, int action, KeyEvent keyEvent) {
            boolean eventHandled = false;

            if (action == EditorInfo.IME_ACTION_SEARCH) {
                searchPodcast(textView.getText().toString());
                eventHandled = true;
            }

            return eventHandled;
        }
    }
}
