package hr.magicpot.projectpliva.database.entity;

import com.roomorama.caldroid.CalendarHelper;

import java.util.Date;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;
import hr.magicpot.projectpliva.constants.Constants;

/**
 * Created by xxx on 22.4.2016..
 */
public class PillDay {
    private int id;
    private String pillnumber;
    private int time;
    private String date;
    private int pillType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPillType() {
        return pillType;
    }

    public void setPillType(int pillType) {
        this.pillType = pillType;
    }

    public String getPillnumber() {
        return pillnumber;
    }

    public void setPillnumber(String pillnumber) {
        this.pillnumber = pillnumber;
    }
}
