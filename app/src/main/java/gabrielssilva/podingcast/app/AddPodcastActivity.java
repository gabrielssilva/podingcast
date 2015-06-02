package gabrielssilva.podingcast.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import gabrielssilva.podingcast.app.interfaces.CallbackListener;

public class AddPodcastActivity extends Activity implements CallbackListener {

    private ProgressBar progressBar;
    private ImageButton searchButton;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_podcast);

        this.initViews();
    }


    private void initViews() {
        this.editText = (EditText) this.findViewById(R.id.search_field_add_podcast);
        this.progressBar = (ProgressBar) this.findViewById(R.id.progress_add_podcast);
        this.searchButton = (ImageButton) this.findViewById(R.id.search_add_podcast);
        ImageButton cancelButton = (ImageButton) this.findViewById(R.id.cancel_add_podcast);


        this.searchButton.setOnClickListener(new OnSearchClick());
        cancelButton.setOnClickListener(new OnCancelClick());
    }

    @Override
    public void onSuccess(Object result) {
        this.progressBar.setVisibility(View.GONE);
        this.searchButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailure(Object result) {
        this.progressBar.setVisibility(View.GONE);
        this.searchButton.setVisibility(View.VISIBLE);
    }

    private class OnCancelClick implements View.OnClickListener {


        @Override
        public void onClick(View view) {
            onBackPressed();
        }
    }

    private class OnSearchClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            progressBar.setVisibility(View.VISIBLE);
            searchButton.setVisibility(View.GONE);
        }
    }
}
