package hr.magicpot.projectpliva.custome;

import android.view.View;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidListener;
import com.roomorama.caldroid.CalendarHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;
import hr.magicpot.projectpliva.ActionObservable;
import hr.magicpot.projectpliva.R;
import hr.magicpot.projectpliva.constants.Constants;
import hr.magicpot.projectpliva.database.MySQLiteHelper;
import hr.magicpot.projectpliva.database.entity.PillDay;
import hr.magicpot.projectpliva.fragments.CustomeCalendarFragment;

/**
 * Created by xxx on 18.4.2016..
 */
public class CustomeCaldroidListener extends CaldroidListener {
    @Override
    public void onSelectDate(Date date, View view) {
        Toast.makeText(view.getContext(), "Hold for pill check!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongClickDate(Date date, View view) {
        MySQLiteHelper db = MySQLiteHelper.getInstance(null);
        int time = (int) (CalendarHelper.convertDateToDateTime(date).getStartOfDay().getMilliseconds(TimeZone.getTimeZone("UTC"))/ Constants.DIVIDETIMEBY);

        PillDay pill = db.getDatePillMap().get(time);
        if(pill != null && pill.getPillType() == R.drawable.red_pill){
            db.updateDatePillMap(time, R.drawable.blue_pill);

            ActionObservable.getInstance().updateCalendar();
            Toast.makeText(view.getContext(), "Pill updated.", Toast.LENGTH_SHORT).show();
        }else if(pill != null && pill.getPillType() == R.drawable.blue_pill)
            Toast.makeText(view.getContext(), "Already checked.", Toast.LENGTH_SHORT).show();
    }
}
