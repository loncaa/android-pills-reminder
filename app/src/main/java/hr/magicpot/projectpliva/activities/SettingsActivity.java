package hr.magicpot.projectpliva.activities;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.DateFormatSymbols;

import hr.magicpot.projectpliva.R;
import hr.magicpot.projectpliva.constants.SharedPreferencesHelper;
import hr.magicpot.projectpliva.dialogs.MyDatePicker;
import hr.magicpot.projectpliva.dialogs.MyTimePicker;

public class SettingsActivity extends AppCompatActivity implements MyTimePicker.TimeListener, MyDatePicker.DateListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PreferenceView())
                .commit();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            return true;
        }else if(id == R.id.action_done){
            if(!PreferenceView.pillTypeList.getSummary().equals(getString(R.string.pilltypePrefSummary)) &&
                    !PreferenceView.btnDate.getSummary().equals(getString(R.string.datePrefSummary)) &&
                    !PreferenceView.btnTime.getSummary().equals(getString(R.string.timePrefSummary))){

                SharedPreferencesHelper.setBoolean("initialized", true, getApplicationContext());

                new PillCalendarOrganizer().execute();
                return true;
            }else{
                Toast.makeText(this, R.string.requiredWarning, Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(int y, int m, int d) {
        PreferenceView.btnDate.setSummary(d + ". " + new DateFormatSymbols().getMonths()[m] + " " + y);

        SharedPreferencesHelper.setInteger("year", y, getApplicationContext());
        SharedPreferencesHelper.setInteger("months", m, getApplicationContext());
        SharedPreferencesHelper.setInteger("day", d, getApplicationContext());
    }

    @Override
    public void onTimeSet(int h, int m) {
        PreferenceView.btnTime.setSummary(h+":"+m);

        SharedPreferencesHelper.setInteger("hours", h, getApplicationContext());
        SharedPreferencesHelper.setInteger("minutes", m, getApplicationContext());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class PreferenceView extends PreferenceFragment {
        public static Preference btnDate;
        public static Preference btnTime;
        public static EditTextPreference notificationTitle;
        public static EditTextPreference notificationSubtitle;
        public static ListPreference pillTypeList;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);

            btnDate = findPreference(getString(R.string.btnDate));
            btnTime = findPreference(getString(R.string.btnTime));
            notificationTitle = (EditTextPreference) findPreference(getString(R.string.titleKey));
            notificationSubtitle = (EditTextPreference) findPreference(getString(R.string.subtitleKey));
            pillTypeList = (ListPreference) findPreference(getString(R.string.pillTypeKey));

            setPreferenceListeners();
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        private void setPreferenceListeners(){
            btnDate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new MyDatePicker().show(getFragmentManager(), "DateTag");
                    return true;
                }
            });

            btnTime.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new MyTimePicker().show(getFragmentManager(), "TimeTag");
                    return true;
                }
            });

            //TODO SVI PODACI SU VEC SPREMLJENI U SHAREDPREFF!!!!
            Preference.OnPreferenceChangeListener preferenceChangeListener = new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if(preference instanceof ListPreference) { //pill type
                        int n = Integer.parseInt((String) newValue);
                        preference.setSummary(((ListPreference) preference).getEntries()[n]);

                        //tip pilula spremiti u preference
                    }
                    else { //notification title and subtitle
                        String key = preference.getKey();
                        if(key == getString(R.string.titleKey)){
                            SharedPreferencesHelper.setString("notification_title", (String) newValue, getActivity());
                        }else if(key == getString(R.string.subtitleKey)){
                            SharedPreferencesHelper.setString("notification_subtitle", (String) newValue, getActivity());
                        }

                        preference.setSummary((String) newValue);
                    }

                    return true;
                }
            };

            notificationTitle.setOnPreferenceChangeListener(preferenceChangeListener);
            notificationSubtitle.setOnPreferenceChangeListener(preferenceChangeListener);
            pillTypeList.setOnPreferenceChangeListener(preferenceChangeListener);
        }

    }

    private class PillCalendarOrganizer extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //bude bijelo i ovo se vidi, nakon toga se pojavi fragment!
            Toast.makeText(getApplicationContext(), "Plesase wait a moment...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.w("ASYNC", "Obrada");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
