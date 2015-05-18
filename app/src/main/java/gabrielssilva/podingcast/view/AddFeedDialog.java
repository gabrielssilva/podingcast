package gabrielssilva.podingcast.view;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import gabrielssilva.podingcast.app.R;
import gabrielssilva.podingcast.app.interfaces.CallbackListener;
import gabrielssilva.podingcast.controller.PodcastController;

public class AddFeedDialog extends DialogFragment {

    public final static String TAG = "ADD_FEED_DIALOG";

    private View rootView;
    private CallbackListener callbackListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        this.rootView = View.inflate(getActivity(), R.layout.dialog_add_feed, null);

        builder.setView(rootView);
        builder.setPositiveButton(R.string.dialog_add_feed_confirm, new OnAddListener());
        builder.setNegativeButton(R.string.dialog_add_feed_cancel, new OnCancelListener());

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.callbackListener = (CallbackListener) activity;
    }


    private class OnAddListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            TextView textView = (TextView) rootView.findViewById(R.id.dialog_add_feed_prompt);
            String feedAddress = textView.getText().toString();

            PodcastController podcastController = new PodcastController(callbackListener);
            podcastController.fetchPodcast(feedAddress, 0);

            getDialog().dismiss();
        }
    }

    private class OnCancelListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            callbackListener.onFailure(null);
            getDialog().dismiss();
        }
    }

}
