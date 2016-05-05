package hr.magicpot.projectpliva.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.roomorama.caldroid.CalendarHelper;

import java.util.Date;
import java.util.TimeZone;

import hr.magicpot.projectpliva.ActionObservable;
import hr.magicpot.projectpliva.R;
import hr.magicpot.projectpliva.activities.MainActivity;
import hr.magicpot.projectpliva.constants.Action;
import hr.magicpot.projectpliva.constants.Constants;
import hr.magicpot.projectpliva.database.MySQLiteHelper;

/**SETaCTION I GETaCTION POKUSAT KORISTITI*/
public class ActionService extends IntentService {

    public ActionService() {
        super("Action THREAD");
    }

    //TODO KAD APLIKACIJE NEMA U BACKSTACKU NE APDEJTA BAZU
    @Override
    protected void onHandleIntent(Intent intent) {
        //dohvatiti drugacije bazu
        Context c = getApplicationContext();
        MySQLiteHelper db = MySQLiteHelper.getInstance(c);

        NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        String action = intent.getStringExtra("action");
        int sDate = (int) (CalendarHelper.convertDateToDateTime(new Date()).getStartOfDay().getMilliseconds(TimeZone.getTimeZone("UTC")) / Constants.DIVIDETIMEBY);

        if (action != null && action.equals(Action.CLOSE.name())){
            db.updateDatePillMap(sDate, R.drawable.red_pill);
        } else if (action != null && action.equals(Action.CONFIRM.name())) {
            db.updateDatePillMap(sDate, R.drawable.blue_pill);
        }


        Log.i("DB", db.getDatabaseName());
        //ugasi alarm
        nm.cancel(Constants.ALARM_SERVICE_ID);
        ActionObservable.getInstance().updateCalendar();
    }
}
