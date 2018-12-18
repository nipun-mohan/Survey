package nimble.survey;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Pref {

    public static SharedPreferences sharedPreferences = null;

    public synchronized static void openPref(Context context) {
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences("SURVEYPREFS", Context.MODE_PRIVATE);
    }

    public static void clear() {
        Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
        prefsPrivateEditor.clear();
        prefsPrivateEditor.commit();
    }

    public static String getValue(String key, String defaultValue) {

        String result = Pref.sharedPreferences.getString(key, defaultValue);

        return result;
    }

    public static boolean getBoolValue(String key, boolean defaultValue) {

        if (Pref.sharedPreferences != null) {
            boolean result = Pref.sharedPreferences.getBoolean(key, defaultValue);

            return result;
        }
        return false;
    }

    public static void setValue(String key, String value) {

        Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
        prefsPrivateEditor.putString(key, value);
        prefsPrivateEditor.commit();


    }

    public static void setIntValue(String key, int value) {

        Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
        prefsPrivateEditor.putInt(key, value);
        prefsPrivateEditor.commit();


    }

    public static int getIntValue(String key, int defaultValue) {

        int result = Pref.sharedPreferences.getInt(key, defaultValue);

        return result;
    }

    public static void setBoolValue(String key, boolean value) {
        Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
        prefsPrivateEditor.putBoolean(key, value);
        prefsPrivateEditor.commit();
    }

    public enum TYPE {
        ACCESS_TOKEN, EXPIRY, TOKEN_FETCH_TIME;
    }


}
