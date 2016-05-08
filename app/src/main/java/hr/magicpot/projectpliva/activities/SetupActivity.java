package hr.magicpot.projectpliva.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.roomorama.caldroid.CalendarHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import hr.magicpot.projectpliva.R;
import hr.magicpot.projectpliva.constants.Constants;
import hr.magicpot.projectpliva.database.MySQLiteHelper;
import hr.magicpot.projectpliva.database.entity.PillDay;
import hr.magicpot.projectpliva.dialogs.MyTimePicker;
import hr.magicpot.projectpliva.receivers.AlarmReceiver;

public class SetupActivity extends AppCompatActivity implements MyTimePicker.TimeListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        Toolbar toolbar = (Toolbar) findViewById(R.id.setup_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences preferences = this.getSharedPreferences("pliva", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Intent i = new Intent(this, AlarmReceiver.class);
        //manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        //pendingIntent = PendingIntent.getBroadcast(this, Constants.ALARM_SERVICE_ID, i, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home){
            Toast.makeText(this, "Home Btn", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    {
        manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        editor.putBoolean("initialized", true);
        editor.commit();

        reset.setVisible(true);
        addTime.setVisible(false);

        mViewPager.setCurrentItem(1);

        Toast.makeText(getApplicationContext(),"Reminder setted.", Toast.LENGTH_LONG).show();
    }*/

    @Override
    public void onTimeSet(int hourOfDay, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.HOUR, hourOfDay);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        //calendar.add(Calendar.DAY_OF_YEAR, 0);

        //editor.putInt("minutes", minutes);
        //editor.putInt("hourOfDay", hourOfDay);
        //editor.commit();

        MySQLiteHelper db = MySQLiteHelper.getInstance(getApplicationContext());
        List<PillDay> list = generateList(2, 21, 7);
        db.setPillList(list); //sprema listu
    }

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

                int time = (int) (CalendarHelper.convertDateToDateTime(calendar.getTime()).getStartOfDay().getMilliseconds(TimeZone.getTimeZone("UTC"))/ Constants.DIVIDETIMEBY);
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
