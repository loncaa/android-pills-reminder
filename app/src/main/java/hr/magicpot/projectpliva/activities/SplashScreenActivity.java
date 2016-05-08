package hr.magicpot.projectpliva.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import hr.magicpot.projectpliva.constants.SharedPreferencesHelper;
import hr.magicpot.projectpliva.database.MySQLiteHelper;

/**
 * Created by xxx on 2.5.2016..
 */
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean init = SharedPreferencesHelper.getBooleanSharedPreference("initialized", getApplicationContext());

        //ako je vec inicijalizirnao vrijeme pozivanja notifikacije, onda dohvati podatke iz baze
        //otvori calendarActivity u suprotnome mainActivity
        if(init) {
            MySQLiteHelper db = MySQLiteHelper.getInstance(this);
            db.fetchDatePillMap();

            Intent i = new Intent(this, CalendarActivity.class);
            startActivity(i);
            finish();
        }
        else {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}
