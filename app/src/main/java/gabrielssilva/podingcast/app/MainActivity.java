package gabrielssilva.podingcast.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import gabrielssilva.podingcast.app.interfaces.CallbackListener;
import gabrielssilva.podingcast.controller.PodcastController;


public class MainActivity extends Activity implements CallbackListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void onRegisterButtonClick(View view) {
        TextView podcastNameTextView = (TextView) findViewById(R.id.textfield_podcast_name);
        TextView rssAddressTextView = (TextView) findViewById(R.id.textfield_rss_address);

        String podcastName = podcastNameTextView.getText().toString();
        String rssAddress = rssAddressTextView.getText().toString();

        PodcastController podcastController = new PodcastController(this);
        podcastController.sendPodcast(podcastName, rssAddress);
    }

    @Override
    public void onSuccess(Object result) {

    }

    @Override
    public void onFailure() {

    }
}
