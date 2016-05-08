package hr.magicpot.projectpliva.activities;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.roomorama.caldroid.CaldroidFragment;

import hr.magicpot.projectpliva.R;
import hr.magicpot.projectpliva.constants.Constants;
import hr.magicpot.projectpliva.constants.SharedPreferencesHelper;
import hr.magicpot.projectpliva.custome.CustomeCaldroidListener;
import hr.magicpot.projectpliva.database.MySQLiteHelper;
import hr.magicpot.projectpliva.fragments.CustomeCalendarFragment;
import hr.magicpot.projectpliva.receivers.AlarmReceiver;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.calendar_toolbar);
        setSupportActionBar(toolbar);

        new FragmentReplace().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reset) {
            SharedPreferencesHelper.remoteSharedPreference("initialized",getApplicationContext());
            SharedPreferencesHelper.remoteSharedPreference("minutes", getApplicationContext());
            SharedPreferencesHelper.remoteSharedPreference("hourOfDay", getApplicationContext());
            SharedPreferencesHelper.remoteSharedPreference("year", getApplicationContext());
            SharedPreferencesHelper.remoteSharedPreference("months",getApplicationContext());
            SharedPreferencesHelper.remoteSharedPreference("day",getApplicationContext());

            MySQLiteHelper database = MySQLiteHelper.getInstance(this);
            database.deleteAllPills();
            database.onUpgrade(database.getWritableDatabase(), 0, 0);

            cancelNotifications();

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void cancelNotifications(){
        Intent i = new Intent(this, AlarmReceiver.class);
        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, Constants.ALARM_SERVICE_ID, i, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(pendingIntent);

        NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(Constants.ALARM_SERVICE_ID);
    }

    private class FragmentReplace extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params) {
            CustomeCalendarFragment calendarFragment = new CustomeCalendarFragment();
            calendarFragment.setCaldroidListener(new CustomeCaldroidListener());

            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.replace(R.id.calendar, calendarFragment);
            t.commit();

            return null;
        }
    }
}
