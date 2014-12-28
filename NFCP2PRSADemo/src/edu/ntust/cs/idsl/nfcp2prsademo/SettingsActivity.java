package edu.ntust.cs.idsl.nfcp2prsademo;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SettingsActivity extends PreferenceActivity implements OnPreferenceClickListener {

    private static final String ACTION = "edu.ntust.cs.idsl.nfctagaesdemo.action.SETTINGS";
    private static final String PUBLIC_KEY = "public_key";
    private static final String PRIVATE_KEY = "private_key";
    private static final String SECRET_KEY = "secret_key";
    private MyApplication app;
    private Preference prefPublicKey;
    private Preference prefPrivateKey;
    private Preference prefSecretKey;
    private String publicKey;
    private String privateKey;
    private String secretKey;

    public static void startAction(Context context) {
        context.startActivity(getAction(context));
    }

    public static Intent getAction(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        intent.setAction(ACTION);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        app = (MyApplication) getApplicationContext();
        setPrefPublicKey();
        setPrefPrivateKey();
        setPrefSecretKey();
    }

    private void setPrefPublicKey() {
        prefPublicKey = (Preference) findPreference(PUBLIC_KEY);
        publicKey = app.getSettings().getPublicKey();
//        if (publicKey.isEmpty()) {
//            prefPublicKey.setSummary(R.string.empty);
//        } else {
//            prefPublicKey.setSummary(publicKey);
//        }
        prefPublicKey.setOnPreferenceClickListener(this);
    }
    
    private void setPrefPrivateKey() {
        prefPrivateKey = (Preference) findPreference(PRIVATE_KEY);
        privateKey = app.getSettings().getPrivateKey();
//        if (privateKey.isEmpty()) {
//            prefPrivateKey.setSummary(R.string.empty);
//        } else {
//            prefPrivateKey.setSummary(privateKey);
//        }     
        prefPrivateKey.setOnPreferenceClickListener(this);
    }
    
    private void setPrefSecretKey() {
        prefSecretKey = (Preference) findPreference(SECRET_KEY);
        secretKey = app.getSettings().getSecretKey();
        if (secretKey.isEmpty()) {
            prefSecretKey.setSummary(R.string.empty);
        } else {
            prefSecretKey.setSummary(secretKey);
        }
        prefSecretKey.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            return true;
        case R.id.action_save:
            saveSettings();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (prefPublicKey == preference) {
            showPublicKeyDialog();
            return true;
        }
        if (prefPrivateKey == preference) {
            showPrivateKeyDialog();
            return true;
        }
        if (prefSecretKey == preference) {
            showGeneratingKeyDialog();
            return true;
        }
        return false;
    }

    private void showPublicKeyDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.pref_public_key)
                .setMessage(app.getSettings().getPublicKey())
                .setPositiveButton(android.R.string.ok, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
    }
    
    private void showPrivateKeyDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.pref_private_key)
                .setMessage(app.getSettings().getPrivateKey())
                .setPositiveButton(android.R.string.ok, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
    }    
    
    private void showGeneratingKeyDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.generate_key)
                .setPositiveButton(android.R.string.ok, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            byte[] newKey = AESCoder.generateKey();
                            secretKey = AESCoder.base64(newKey);
                            prefSecretKey.setSummary(secretKey);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }}
                ).create().show();
    }

    private void saveSettings() {
        app.getSettings().setSecretKey(secretKey);
        ToastMaker.toast(this, R.string.your_settings_have_been_saved);
    }

}
