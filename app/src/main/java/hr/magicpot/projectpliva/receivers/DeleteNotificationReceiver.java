package hr.magicpot.projectpliva.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.roomorama.caldroid.CalendarHelper;

import java.util.Date;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;
import hr.magicpot.projectpliva.ActionObservable;
import hr.magicpot.projectpliva.R;
import hr.magicpot.projectpliva.activities.MainActivity;
import hr.magicpot.projectpliva.constants.Constants;
import hr.magicpot.projectpliva.database.MySQLiteHelper;
import hr.magicpot.projectpliva.database.entity.PillDay;

/**
 * Created by xxx on 3.5.2016..
 */
public class DeleteNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long d = intent.getLongExtra("date", 0);
        Date date = new Date(d);

        MySQLiteHelper db = MySQLiteHelper.getInstance(context);
        Log.i("DB", db.getDatabaseName());

        int time = (int) (CalendarHelper.convertDateToDateTime(date).getStartOfDay().getMilliseconds(TimeZone.getTimeZone("UTC"))/ Constants.DIVIDETIMEBY);
        if(db.doesExist(time)){
            db.updateDatePillMap(time, R.drawable.red_pill);
            ActionObservable.getInstance().updateCalendar();
        }

    }
}
