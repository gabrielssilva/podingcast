package gabrielssilva.podingcast.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import gabrielssilva.podingcast.controller.PodcastController;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onRegisterButtonClick(View view) {
        TextView podcastNameTextView = (TextView) findViewById(R.id.textfield_podcast_name);
        TextView rssAddressTextView = (TextView) findViewById(R.id.textfield_rss_address);

        String podcastName = podcastNameTextView.getText().toString();
        String rssAddress = rssAddressTextView.getText().toString();

        PodcastController controllerInstance = PodcastController.getInstance();
        controllerInstance.sendPodcast(podcastName, rssAddress);
    }
}
