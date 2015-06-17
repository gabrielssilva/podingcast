package gabrielssilva.podingcast.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import gabrielssilva.podingcast.app.R;
import gabrielssilva.podingcast.app.interfaces.CallbackListener;

public class DeleteEpisodeDialog extends DialogFragment {

    public static final String TAG = "DELETE_EPISODE_DIALOG";

    private static final String ARG_ITEM_NAME = "item_name";
    private CallbackListener callbackListener;

    public static DeleteEpisodeDialog newInstance(String itemName) {
        DeleteEpisodeDialog dialogFragment = new DeleteEpisodeDialog();
        Bundle args = new Bundle();
        args.putString(ARG_ITEM_NAME, itemName);

        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String itemName = getArguments().getString(ARG_ITEM_NAME);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getResources().getString(R.string.remove) + " " + itemName + "?")
                .setPositiveButton(R.string.yes, new ConfirmDialogListener())
                .setNegativeButton(R.string.no, new CancelDialogListener());

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        this.callbackListener = (CallbackListener) activity;
    }


    private class ConfirmDialogListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            callbackListener.onSuccess(null);
        }
    }

    private class CancelDialogListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            callbackListener.onFailure(null);
        }
    }
}
