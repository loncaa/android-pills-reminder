package hr.magicpot.projectpliva.fragments;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.util.Observable;
import java.util.Observer;

import hr.magicpot.projectpliva.ActionObservable;
import hr.magicpot.projectpliva.custome.CustomeCaldroidListener;
import hr.magicpot.projectpliva.custome.CustomeCalendarAdapter;
import hr.magicpot.projectpliva.database.MySQLiteHelper;

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

            MySQLiteHelper db = MySQLiteHelper.getInstance(getContext());
            if(MySQLiteHelper.datePillMap.isEmpty()){
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
