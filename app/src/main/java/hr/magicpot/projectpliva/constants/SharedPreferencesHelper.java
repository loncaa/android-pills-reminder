package hr.magicpot.projectpliva.constants;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Objects;

/**
 * Created by xxx on 3.5.2016..
 */
public final class SharedPreferencesHelper {

    public static boolean getBooleanSharedPreference(String key, Context c){
        return c.getSharedPreferences("pliva", Context.MODE_PRIVATE).getBoolean(key, false);
    }

    public static void setBooleanSharedPreference(String key, boolean value, Context c) {
        SharedPreferences preferences = c.getSharedPreferences("pliva", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static int getIntegerSharedPreference(String key, Context c){
        return c.getSharedPreferences("pliva", Context.MODE_PRIVATE).getInt(key, -1);
    }

    public static void setIntegerSharedPreference(String key, int value, Context c) {
        SharedPreferences preferences = c.getSharedPreferences("pliva", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void remoteSharedPreference(String key, Context c){
        SharedPreferences preferences = c.getSharedPreferences("pliva", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }


}
