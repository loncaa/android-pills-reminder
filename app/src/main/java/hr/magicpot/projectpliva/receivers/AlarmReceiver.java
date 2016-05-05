package hr.magicpot.projectpliva.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;

import java.util.Calendar;

import hr.magicpot.projectpliva.constants.Constants;
import hr.magicpot.projectpliva.services.AlarmService;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Constants.ALARM_SERVICE_ID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        SharedPreferences sharedPreferences = context.getSharedPreferences("pliva", Context.MODE_PRIVATE);
        int minute = sharedPreferences.getInt("minutes", 0);
        int hour = sharedPreferences.getInt("hourOfDay", 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, minute );
        calendar.set(Calendar.HOUR, hour );
        calendar.set(Calendar.AM_PM, Calendar.AM);
        //calendar.add(Calendar.DAY_OF_YEAR, 1);

        if(calendar.before(Calendar.getInstance())){
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        //TODO ako je to odabrano vrijeme proslo onda odma pozove
        //provjeriti dali je proslo vrijeme
        manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        //manager.setExact(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()+1000*5, pendingIntent);

        Intent i = new Intent(context, AlarmService.class);
        context.startService(i);
    }
}
