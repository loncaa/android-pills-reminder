package hr.magicpot.projectpliva.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

import hr.magicpot.projectpliva.constants.Constants;

/**
 * Created by xxx on 6.4.2016..
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AlarmReceiver.class);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Constants.ALARM_SERVICE_ID, i, PendingIntent.FLAG_CANCEL_CURRENT);

        SharedPreferences preferences = context.getSharedPreferences("pliva", Context.MODE_PRIVATE);
        int minutes = preferences.getInt("minutes", 0);
        int hourOfDay = preferences.getInt("hourOfDay", 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.HOUR, hourOfDay);
        calendar.set(Calendar.AM_PM, Calendar.AM);

        if(calendar.before(Calendar.getInstance())){
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        //manager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), 1000, pendingIntent);
        manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
