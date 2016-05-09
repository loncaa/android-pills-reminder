package hr.magicpot.projectpliva.constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.BoringLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by xxx on 3.5.2016..
 */
public final class SharedPreferencesHelper {

    private static Map<String, Object> keysMap = new HashMap<>(); //sprema sve keyove od sharedprefa koje se trenutno koriste u appu

    public static boolean getBoolean(String key, Context c){
        return c.getSharedPreferences("pliva", Context.MODE_PRIVATE).getBoolean(key, false);
    }

    public static void setBoolean(String key, Boolean value, Context c) {
        SharedPreferences preferences = c.getSharedPreferences("pliva", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();

        keysMap.put(key, value);
    }

    public static int getInteger(String key, Context c){
        return c.getSharedPreferences("pliva", Context.MODE_PRIVATE).getInt(key, -1);
    }

    public static void setInteger(String key, Integer value, Context c) {
        SharedPreferences preferences = c.getSharedPreferences("pliva", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();

        keysMap.put(key, value);
    }

    public static void setString(String key, String value, Context c) {
        SharedPreferences preferences = c.getSharedPreferences("pliva", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();

        keysMap.put(key, value);
    }

    public static String getString(String key, Context c){
        return c.getSharedPreferences("pliva", Context.MODE_PRIVATE).getString(key, "err");
    }

    public static void removeValue(String key, Context c){
        SharedPreferences preferences = c.getSharedPreferences("pliva", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static void removeAllValues(Context c){
        SharedPreferences preferences = c.getSharedPreferences("pliva", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply(); //.commit()
    }


}
