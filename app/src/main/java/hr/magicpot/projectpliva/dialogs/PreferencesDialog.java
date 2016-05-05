package hr.magicpot.projectpliva.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.BindString;
import butterknife.ButterKnife;
import hr.magicpot.projectpliva.R;

/**
 * Dialog koji se otvara nakon odabira vremena
 * sluzi za postavljanje nekih custom borojeca, ukoliko korisnik ne zeli postalvjati brojeve samo pritisne tipku uredu
 * biti ce postavljeni defaultni brojevi
 */
public class PreferencesDialog extends DialogFragment {

    @BindString(R.string.setting_done) String mDone;
    @BindString(R.string.setting_cancel) String mCancel;
    @BindString(R.string.setting_title) String mTitle;

    DialogListener activity;

    public interface DialogListener{
        void onPositiveButtonClick(DialogInterface dialog);
        void onNegativeButtonClick(DialogInterface dialog);
    }

    private DialogInterface.OnClickListener positiveButton = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            activity.onPositiveButtonClick((DialogInterface) dialog);
        }
    };

    private DialogInterface.OnClickListener negativeButton = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            activity.onNegativeButtonClick((DialogInterface) dialog);
        }
    };

    public PreferencesDialog() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (DialogListener) activity;

        ButterKnife.bind(activity);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_settings, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //builder.setView(view);
        builder.setTitle("SETTINGS");
        builder.setPositiveButton("Done", positiveButton);
        builder.setNegativeButton("Cancel", negativeButton);

        return builder.create();
    }
}
