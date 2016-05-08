package hr.magicpot.projectpliva.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.app.DialogFragment; //promjeniti jednog dana u 'support.v4.app.'

import com.roomorama.caldroid.CalendarHelper;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by xxx on 5.4.2016..
 */
public class MyTimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener  {

    private TimeListener activity;

    public interface TimeListener{
        void onTimeSet(int hourOfDay, int minutes);
    }

    //TODO NE POSTAVI am VRIJEME
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date d = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.AM_PM, Calendar.AM);

        return new TimePickerDialog(getActivity(), this, c.get(Calendar.HOUR), c.get(Calendar.MINUTE), true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (TimeListener) activity;
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        activity.onTimeSet(hourOfDay, minute); //salje podatke u main activity
    }
}
