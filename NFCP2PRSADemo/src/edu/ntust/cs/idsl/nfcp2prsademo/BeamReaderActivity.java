package edu.ntust.cs.idsl.nfcp2prsademo;

import org.ndeftools.Message;
import org.ndeftools.MimeRecord;
import org.ndeftools.Record;
import org.ndeftools.externaltype.ExternalTypeRecord;
import org.ndeftools.util.activity.NfcReaderActivity;
import org.ndeftools.wellknown.TextRecord;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import edu.ntust.cs.idsl.nfcp2prsademo.R;

public class BeamReaderActivity extends NfcReaderActivity {

    private static final String TAG = BeamReaderActivity.class.getName();
    private static final String ACTION = "edu.ntust.cs.idsl.nfctagdemo.action.READ_TAG";
    protected Message message;
    private Button buttonCancel;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, BeamReaderActivity.class);
        intent.setAction(ACTION);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.activity_beam_reader);
        setDetecting(true);

        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void nfcIntentDetected(Intent intent, String action) {
        super.nfcIntentDetected(intent, action);
        vibrate(200);
    }    
    
    /**
     * An NDEF message was read and parsed. This method prints its contents to
     * log and then shows its contents in the GUI.
     * 
     * @param message the message
     */
    @Override
    public void readNdefMessage(Message message) {
        if (message.size() > 1) {
            // ToastMaker.toast(this, R.string.readMultipleRecordNDEFMessage);
        } else {
            // ToastMaker.toast(this, R.string.readSingleRecordNDEFMessage);
        }

        this.message = message;

        // process message

        // show in log
        if (message != null) {
            // iterate through all records in message
            Log.d(TAG, "Found " + message.size() + " NDEF records");

            for (int k = 0; k < message.size(); k++) {
                Record record = message.get(k);

                Log.d(TAG, "Record " + k + " type "
                        + record.getClass().getSimpleName());

                // your own code here, for example:
                if (record instanceof MimeRecord) {
                    // ..
                } else if (record instanceof ExternalTypeRecord) {
                    // ..
                } else if (record instanceof TextRecord) {
                    // ..
                } else { // more else
                    // ..
                }
            }
        }

        setResult(Activity.RESULT_OK,
                BeamReaderFragment.getIntentForResult(message));
        finish();
    }

    /**
     * An empty NDEF message was read.
     * 
     */
    @Override
    protected void readEmptyNdefMessage() {
        ToastMaker.toast(this, R.string.readEmptyMessage);
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    /**
     * 
     * Something was read via NFC, but it was not an NDEF message.
     * 
     * Handling this situation is out of scope of this project.
     * 
     */
    @Override
    protected void readNonNdefMessage() {
        ToastMaker.toast(this, R.string.readNonNDEFMessage);
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    /**
     * 
     * NFC feature was found and is currently enabled
     * 
     */
    @Override
    protected void onNfcStateEnabled() {
        // ToastMaker.toast(this, R.string.nfcAvailableEnabled);
    }

    /**
     * 
     * NFC feature was found but is currently disabled
     * 
     */
    @Override
    protected void onNfcStateDisabled() {
        ToastMaker.toast(this, R.string.nfcAvailableDisabled);
        startNfcSettingsActivity();
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    /**
     * 
     * NFC setting changed since last check. For example, the user enabled NFC
     * in the wireless settings.
     * 
     */
    @Override
    protected void onNfcStateChange(boolean enabled) {
        if (enabled) {
            ToastMaker.toast(this, R.string.nfcAvailableEnabled);
        } else {
            ToastMaker.toast(this, R.string.nfcAvailableDisabled);
        }
    }

    /**
     * 
     * This device does not have NFC hardware
     * 
     */
    @Override
    protected void onNfcFeatureNotFound() {
        ToastMaker.toast(this, R.string.noNfcMessage);
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    private String getType(String type) {
        if (Ndef.NFC_FORUM_TYPE_1.equals(type))
            return new String("NFC Forum Type 1 Tag ");
        if (Ndef.NFC_FORUM_TYPE_2.equals(type))
            return new String("NFC Forum Type 2 Tag ");
        if (Ndef.NFC_FORUM_TYPE_3.equals(type))
            return new String("NFC Forum Type 3 Tag ");
        if (Ndef.NFC_FORUM_TYPE_4.equals(type))
            return new String("NFC Forum Type 4 Tag ");
        if (Ndef.MIFARE_CLASSIC.equals(type))
            return new String("MIFARE Classic Tag ");
        return null;
    }
   
    public void vibrate(int duration) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(duration);
    }
    
}
