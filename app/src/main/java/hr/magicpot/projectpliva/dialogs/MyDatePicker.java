package hr.magicpot.projectpliva.dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment; // 'support.v4.app.'
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

import hr.magicpot.projectpliva.activities.SettingsActivity;

/**
 * Created by xxx on 8.5.2016..
 */
public class MyDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private DateListener activity;

    public interface DateListener{
        void onDateSet(int y, int m, int d);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        return new DatePickerDialog(getActivity(), this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (DateListener) activity;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        activity.onDateSet(year, monthOfYear, dayOfMonth);
    }
}
