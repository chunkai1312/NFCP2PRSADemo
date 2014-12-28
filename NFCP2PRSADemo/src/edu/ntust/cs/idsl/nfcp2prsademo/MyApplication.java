package edu.ntust.cs.idsl.nfcp2prsademo;

import android.app.Application;

public class MyApplication extends Application {

    private SettingsManager settings;

    @Override
    public void onCreate() {
        super.onCreate();
        settings = SettingsManager.getInstance(getApplicationContext());
        initKey();
    }

    public SettingsManager getSettings() {
        return settings;
    }
    
    private void initKey() {
        if (settings.getPrivateKey().isEmpty()) 
            settings.setPrivateKey(getString(R.string.default_private_key));
        
        if (settings.getPublicKey().isEmpty()) 
            settings.setPublicKey(getString(R.string.default_public_key));
    }

}
