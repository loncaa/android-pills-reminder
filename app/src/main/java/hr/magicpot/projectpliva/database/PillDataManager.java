package hr.magicpot.projectpliva.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * TODO prebaciti sve DAO metode iz MySQLHelpera ovdje
 * singleton klasa, MySqlHelper ne treba biti singleton
 */
public class PillDataManager {
    private SQLiteDatabase db;
    private MySQLiteHelper helper;
    private String[] allColumns = new String[]{ MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_DATE, MySQLiteHelper.COLUMN_PILLTYPE};

}
