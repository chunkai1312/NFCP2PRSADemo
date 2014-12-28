package edu.ntust.cs.idsl.nfcp2prsademo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SettingsManager {

    private static SettingsManager instance;

    private Context context;
    private SharedPreferences pref;
    private Editor editor;

    public static final String PREF_NAME = "SettingsPref";
    public static final String PUBLIC_KEY = "public_key";
    public static final String PRIVATE_KEY = "private_key";
    public static final String SECRET_KEY = "secret_key";

    private SettingsManager(Context context) {
        this.context = context;
        pref = this.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static synchronized SettingsManager getInstance(Context context) {
        if (instance == null) {
            instance = new SettingsManager(context);
        }
        return instance;
    }

    public void setPublicKey(String value) {
        editor.putString(PUBLIC_KEY, value);
        editor.commit();
    }

    public String getPublicKey() {
        return pref.getString(PUBLIC_KEY, "");
    }   
    
    public void setPrivateKey(String value) {
        editor.putString(PRIVATE_KEY, value);
        editor.commit();
    }

    public String getPrivateKey() {
        return pref.getString(PRIVATE_KEY, "");
    }
    
    public void setSecretKey(String value) {
        editor.putString(SECRET_KEY, value);
        editor.commit();
    }

    public String getSecretKey() {
        return pref.getString(SECRET_KEY, "");
    }

}
