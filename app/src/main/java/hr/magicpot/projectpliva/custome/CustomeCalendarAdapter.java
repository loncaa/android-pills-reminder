package hr.magicpot.projectpliva.custome;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidGridAdapter;

import java.util.Map;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;
import hr.magicpot.projectpliva.R;
import hr.magicpot.projectpliva.constants.Constants;
import hr.magicpot.projectpliva.database.MySQLiteHelper;
import hr.magicpot.projectpliva.database.entity.PillDay;
import hr.magicpot.projectpliva.fragments.CustomeCalendarFragment;

/**
 * TODO ZASTO SE INSTANCIRA VISE PUTA?
 */
public class CustomeCalendarAdapter extends CaldroidGridAdapter {
    private final int padding = 10;
    private MySQLiteHelper db;

    //u shacred preferences spremi samo dan pocetka uzimanja pilula!
   private CustomeCalendarFragment fragment;

    public CustomeCalendarAdapter(Context context, int month, int year,
                                       Map<String, Object> caldroidData,
                                       Map<String, Object> extraData) {
        super(context, month, year, caldroidData, extraData);
        db = MySQLiteHelper.getInstance(context);
    }

    //poziva se iako nije bitna, usporava sve!!
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View cellView = convertView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            cellView = inflater.inflate(R.layout.custome_cell, null);
        }

        TextView dayText = (TextView) cellView.findViewById(R.id.day);
        TextView pillNumberText = (TextView) cellView.findViewById(R.id.pillday);
        ImageView pillTypeImage = (ImageView) cellView.findViewById(R.id.pillindicator);

        //datum u kalendaru na određenoj poziciju
        DateTime dateTime = this.datetimeList.get(position); //NEEFIKASNO

        dayText.setTextColor(Color.BLACK);
        if (dateTime.getMonth() != month) {
            dayText.setTextColor(context.getResources().getColor(com.caldroid.R.color.caldroid_darker_gray));
        }

        int time = (int) (dateTime.getStartOfDay().getMilliseconds(TimeZone.getTimeZone("UTC")) / Constants.DIVIDETIMEBY);
        PillDay pillDay = db.datePillMap.get(time);
        if (pillDay != null) {
            pillNumberText.setText(pillDay.getPillnumber());

            //TODO uz pilulu obojati i okvir oko nje u boji pilule!
            //TODO koristiti setImageDrawable ili setImageBitmap
            pillTypeImage.setImageResource(pillDay.getPillType());
        }


        if (dateTime.equals(getToday())) {
            cellView.setBackgroundResource(com.caldroid.R.drawable.red_border);
        } else {
            cellView.setBackgroundResource(com.caldroid.R.drawable.cell_bg);
        }

        dayText.setText(""+dateTime.getDay());

        cellView.setPadding(padding, padding, padding, padding);
        //setCustomResources(dateTime, cellView, dayText);

        //cellView.refreshDrawableState();

        return cellView;
    }
}
