package gabrielssilva.podingcast.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

import gabrielssilva.podingcast.adapter.SearchResultAdapter;
import gabrielssilva.podingcast.app.interfaces.CallbackListener;
import gabrielssilva.podingcast.web.SearchTask;

public class AddPodcastActivity extends Activity implements CallbackListener {

    private ProgressBar progressBar;
    private ListView resultsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_podcast);

        this.initViews();
    }


    private void initViews() {
        this.progressBar = (ProgressBar) this.findViewById(R.id.progress_add_podcast);
        this.resultsList = (ListView) this.findViewById(R.id.results_list);

        EditText editText = (EditText) this.findViewById(R.id.search_field_add_podcast);
        editText.setOnEditorActionListener(new SearchAction());
    }

    private void searchPodcast(String podcastName) {
        SearchTask searchTask = new SearchTask(this);
        searchTask.execute(podcastName);

        progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public void onSuccess(Object result) {
        SearchResultAdapter adapter = new SearchResultAdapter(this, (JSONObject) result);
        this.resultsList.setAdapter(adapter);

        this.progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFailure(Object result) {
        this.progressBar.setVisibility(View.INVISIBLE);
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
