package hr.magicpot.projectpliva.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;
import com.roomorama.caldroid.CalendarHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import hirondelle.date4j.DateTime;
import hr.magicpot.projectpliva.ActionObservable;
import hr.magicpot.projectpliva.constants.Resources;
import hr.magicpot.projectpliva.custome.CustomeCaldroidListener;
import hr.magicpot.projectpliva.custome.CustomeCalendarAdapter;
import hr.magicpot.projectpliva.database.MySQLiteHelper;
import hr.magicpot.projectpliva.database.entity.PillDay;

/**
 * Created by xxx on 17.4.2016..
 */
public class CustomeCalendarFragment extends CaldroidFragment implements Observer {
    public CustomeCalendarFragment(){
        super();
        ActionObservable.getInstance().addObserver(this); //ovdje se pretplati na updateve
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){

            //XXX RUŠI APP
            MySQLiteHelper db = MySQLiteHelper.getInstance(getContext());
            if(db.datePillMap.isEmpty()){
                db.fetchDatePillMap();
            }

            setCaldroidListener(new CustomeCaldroidListener());
            refreshView();
        }
    }

    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        return new CustomeCalendarAdapter(getActivity(), month, year, getCaldroidData(), extraData);
    }

    @Override
    public void update(Observable observable, Object data) {
        //u slicaju da se na notifikaciji dodirne akcija, a aktiviti ugašen, ruši se aplikacija
        if(getContext() != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refreshView();
                }
            });
    }
}
