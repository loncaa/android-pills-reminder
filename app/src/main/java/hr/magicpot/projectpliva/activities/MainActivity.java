package hr.magicpot.projectpliva.activities;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.roomorama.caldroid.CalendarHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Observer;
import java.util.TimeZone;

import hr.magicpot.projectpliva.R;
import hr.magicpot.projectpliva.constants.Constants;
import hr.magicpot.projectpliva.constants.Resources;
import hr.magicpot.projectpliva.database.MySQLiteHelper;
import hr.magicpot.projectpliva.database.entity.PillDay;
import hr.magicpot.projectpliva.dialogs.MyTimePickerDialog;
import hr.magicpot.projectpliva.dialogs.PreferencesDialog;
import hr.magicpot.projectpliva.fragments.AddpillsFragment;
import hr.magicpot.projectpliva.fragments.CustomeCalendarFragment;
import hr.magicpot.projectpliva.fragments.NotificationFragment;
import hr.magicpot.projectpliva.receivers.AlarmReceiver;

public class MainActivity extends AppCompatActivity implements MyTimePickerDialog.TimeListener, PreferencesDialog.DialogListener{


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private CustomeCalendarFragment calendarFragment;
    private Calendar calendar;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private  AlarmManager manager;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //mViewPager.setPageTransformer(false, new FadeTransformer());

        calendarFragment = new CustomeCalendarFragment();
        //calendarFragment.setUserVisibleHint(false);
        //calendarFragment = new CaldroidFragment();

        preferences = this.getSharedPreferences("pliva", MODE_PRIVATE);
        editor = preferences.edit();

        Intent i = new Intent(this, AlarmReceiver.class);
        manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        pendingIntent = PendingIntent.getBroadcast(this, Constants.ALARM_SERVICE_ID, i, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(Resources.getInstance().isTimepickerFragment()){
            mViewPager.setCurrentItem(1);
        }
        else {
            mViewPager.setCurrentItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reset) {

            editor.remove("initialized");
            editor.remove("minutes");
            editor.remove("hourOfDay");
            editor.commit();

            MySQLiteHelper database = MySQLiteHelper.getInstance(this);
            database.deleteAllPills();
            database.onUpgrade(database.getWritableDatabase(), 0, 0);

            manager.cancel(pendingIntent);

            NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(Constants.ALARM_SERVICE_ID);

            mViewPager.setCurrentItem(0);
            Toast.makeText(getApplicationContext(),"Reminder reseted.", Toast.LENGTH_LONG).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //TODO PROVJERITI STA SE DOGODI KAD SE NE ODABERE VRIJEME!
    @Override
    public void onTimeSet(int hourOfDay, int minutes) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.HOUR, hourOfDay);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        //calendar.add(Calendar.DAY_OF_YEAR, 0);

        editor.putInt("minutes", minutes);
        editor.putInt("hourOfDay", hourOfDay);
        editor.commit();

        MySQLiteHelper db = MySQLiteHelper.getInstance(getApplicationContext());
        List<PillDay> list = generateList(2, 21, 7);
        db.setPillList(list); //sprema listu
    }

    @Override
    public void onPositiveButtonClick(DialogInterface dialog) {
        manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        editor.putBoolean("initialized", true);
        editor.commit();
        mViewPager.setCurrentItem(1);

        Toast.makeText(getApplicationContext(),"Reminder setted.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNegativeButtonClick(DialogInterface dialog) {
        Toast.makeText(this, "Maybe next time.", Toast.LENGTH_SHORT).show();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0:
                    //predpostavlja se da se krece piti od danasnjega dana
                    //u novijoj aplikaciji napraviti da se moze krenuti od n-tog dana
                    return new AddpillsFragment();
                case 1:
                    return calendarFragment;
                case 2:
                    return new NotificationFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Add pill";
                case 1:
                    return "Calendar";
                case 3:
                    return "Notification";
            }
            return null;
        }
    }

    //
    private List<PillDay> generateList(int nSeries, int pillDays, int pillBreak){
        //puni listu List<PillDay>
        List<PillDay> list = new ArrayList<>();
        for(int j = 0; j < nSeries; j++)
        {
            for(int i = 0, id = 1; i < pillDays; i++, id++)
            {
                Calendar calendar = Calendar.getInstance();
                PillDay p = new PillDay();
                calendar.add(Calendar.DAY_OF_YEAR, i + (pillDays*j) + (pillBreak*j));

                int time = (int) (CalendarHelper.convertDateToDateTime(calendar.getTime()).getStartOfDay().getMilliseconds(TimeZone.getTimeZone("UTC"))/Constants.DIVIDETIMEBY);
                p.setPillType(R.drawable.gray_pill);
                p.setTime(time);
                p.setPillnumber(Integer.toString(id));
                list.add(p);
            }
            for (int i = 0; i < pillBreak; i++){

                Calendar calendar = Calendar.getInstance();
                PillDay p = new PillDay();
                calendar.add(Calendar.DAY_OF_YEAR, i + (pillDays * (j + 1)));

                int time = (int) (CalendarHelper.convertDateToDateTime(calendar.getTime()).getStartOfDay().getMilliseconds(TimeZone.getTimeZone("UTC"))/Constants.DIVIDETIMEBY);
                p.setPillType(R.drawable.cell_bg);
                p.setTime(time);
                list.add(p);
            }
        }

        return list;
    }
}
