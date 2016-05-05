package hr.magicpot.projectpliva.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import com.roomorama.caldroid.CalendarHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hirondelle.date4j.DateTime;
import hr.magicpot.projectpliva.constants.Resources;
import hr.magicpot.projectpliva.database.entity.PillDay;

/**
 * TODO napraviti dohvacanje s više niti?
 */
public class MySQLiteHelper extends SQLiteOpenHelper {
    private static MySQLiteHelper databaseHelper = null;

    public static final String TABLE_NAME = "PILLSTABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_PILLTYPE = "PILLTYPE";
    public static final String COLUMN_DATE = "PILLDATE";
    public static final String COLUMN_PILLNUMBER = "PILLNUMBER";

    public static final String DATABASE_NAME = "plivacpills.db";
    public static final int DATABASE_VERSION = 1;

    public List<PillDay> cachedList = null;
    public Map<Integer, PillDay> datePillMap = new HashMap<>();

    public static final String DATABASE_CREATE
            = "create table " + TABLE_NAME
            + " ( " + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_PILLTYPE + " integer, "
            + COLUMN_DATE + " integer,"
            + COLUMN_PILLNUMBER + " varchar(4) );";

    private MySQLiteHelper(Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static MySQLiteHelper getInstance(Context c) {
        if(databaseHelper == null)
            return databaseHelper = new MySQLiteHelper(c);
        else return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
        Log.e("PILL_SELECT_QUERY", DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        Log.e("PILL_SELECT_QUERY", "DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean doesExist(long date) {
        SQLiteDatabase db = this.getReadableDatabase();

        //treba mi samo datum a ne i VRIJEME
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " where " + COLUMN_DATE + " = '" + date + "'";
        Log.e("PILL_SELECT_QUERY", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null && c.moveToFirst()){
            return true;
        }
        c.close();

        return false;
    }

    /**dohvati listu iz baze i predaje kroz povratni parametar
     * u međuvremenu spremi listu i kešra je*/
    public List<PillDay> getPills(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<PillDay> pills = null;
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        Log.e("PILL_SELECT_QUERY", "SELECT * FROM " + TABLE_NAME);

        if(c != null && c.moveToFirst())
        {
            pills = new ArrayList<>();
            do{
                PillDay pill = new PillDay();
                pill.setPillnumber(c.getString(c.getColumnIndex(COLUMN_PILLNUMBER)));
                pill.setPillType(c.getInt(c.getColumnIndex(COLUMN_PILLTYPE)));
                pill.setTime(c.getInt(c.getColumnIndex(COLUMN_DATE)));
                pills.add(pill);
            }while (c.moveToNext());
        }
        c.close();

        cachedList = new ArrayList<>(pills); //refresh cached list
        return pills;
    }

    public void setPillList(List<PillDay> pills){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.beginTransaction();
        for(PillDay p : pills)
        {
            values.put(COLUMN_DATE, p.getTime());
            values.put(COLUMN_PILLTYPE, p.getPillType());
            values.put(COLUMN_PILLNUMBER, p.getPillnumber());
            db.insert(TABLE_NAME, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    /**updejta tip pilule u bazi za zadani datum*/
    public void updatePill(int date, int pillType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PILLTYPE, pillType);

        db.beginTransaction();
        db.update(TABLE_NAME, cv, COLUMN_DATE + " = " + date, null);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    /*
    public void deletePill(PillDay p){
        SQLiteDatabase db = this.getWritableDatabase();
        StringBuilder query = new StringBuilder();
        List<String> args = new ArrayList<>();

        query.append(COLUMN_DATE + " = ?, ");
        args.add(String.valueOf(p.getTime()));

        db.delete(TABLE_NAME, query.toString(), args.toArray(new String[0]));
        db.close();
    }
    */

    public void deleteAllPills(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        db.delete(TABLE_NAME, null, null);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();

        cachedList.clear();
        datePillMap.clear();
    }

    /**dohvača listu iz baze i kreira hashmapu koja se kasnije koristi
     * kako bi se smanjlilo cekanje baza-korisnik koristi se hashmapa*/
    public void fetchDatePillMap() {
        List<PillDay> list = getPills();

        if(list != null)
            for(PillDay p : list){
                datePillMap.put(p.getTime(), p);
            }
    }

    public List<PillDay> getCachedList() {
        return cachedList;
    }

    /**vraca hashmapu iz koja bi trebala biti singronizirana sa bazom*/
    public Map<Integer, PillDay> getDatePillMap() {
        return datePillMap;
    }

    /**updejta bazu i mapu koja se koristi kroz aplikacju
     * ako ne postoji mapa onda samo apdejta bazu*/
    public void updateDatePillMap(Integer key, int type){
        updatePill(key, type);

        if(datePillMap != null && datePillMap.get(key) != null)
            datePillMap.get(key).setPillType(type);
    }
}
