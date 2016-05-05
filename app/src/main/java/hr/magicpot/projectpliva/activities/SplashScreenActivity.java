package hr.magicpot.projectpliva.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;

import hr.magicpot.projectpliva.constants.Resources;
import hr.magicpot.projectpliva.database.MySQLiteHelper;
import hr.magicpot.projectpliva.database.entity.PillDay;

/**
 * Created by xxx on 2.5.2016..
 */
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean init = getSharedPreferences("pliva", MODE_PRIVATE).getBoolean("initialized", false);
        Resources.getInstance().setTimepickerFragment(init);

        //ako je vec inicijalizirnao vrijeme pozivanja notifikacije, onda dohvati podatke iz baze
        if(init) {
            MySQLiteHelper db = MySQLiteHelper.getInstance(this);
            db.fetchDatePillMap();
        }

        //moze se staviti putExtra
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
