package edu.ntust.cs.idsl.nfcp2prsademo;

import java.nio.charset.Charset;
import java.util.Locale;

import org.ndeftools.Message;
import org.ndeftools.util.activity.NfcBeamWriterActivity;
import org.ndeftools.wellknown.TextRecord;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;


@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class BeamWriterActivity extends NfcBeamWriterActivity {

	private static final String TAG = BeamWriterActivity.class.getName();
	private static final String ACTION = "edu.ntust.cs.idsl.nfctagdemo.action.BEAM_MESSAGE";
	private static final String EXTRA_SECRET_KEY = "edu.ntust.cs.idsl.nfctagdemo.extra.SECRET_KEY";
	private static final String EXTRA_CIPHER_TEXT = "edu.ntust.cs.idsl.nfctagdemo.extra.CIPHERTEXT";
	private static final int MESSAGE_SENT = 1;
	protected Message message;
	private String secretKey;
	private String ciphertext;
	private Button buttonCancel;
	
    public static Intent getIntent(Context context, String secretKey, String ciphertext) {
        Intent intent = new Intent(context, BeamWriterActivity.class);
        intent.setAction(ACTION);
        intent.putExtra(EXTRA_SECRET_KEY, secretKey);
        intent.putExtra(EXTRA_CIPHER_TEXT, ciphertext);
        return intent;
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setContentView(R.layout.activity_beam_writer);	
		setDetecting(true);
		startPushing();
		
		secretKey = getIntent().getStringExtra(EXTRA_SECRET_KEY);
		ciphertext = getIntent().getStringExtra(EXTRA_CIPHER_TEXT);
        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
	}
	
    /**
	 * 
	 * Implementation of {@link CreateNdefMessageCallback} interface.
	 * 
	 * This method is called when another device is within reach (communication is to take place).
	 */
	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		Log.d(TAG, "Create message to be beamed");
		
		Message message = new Message();	
		
		TextRecord textRecordSecretKey = new TextRecord();
		textRecordSecretKey.setText(secretKey);
		textRecordSecretKey.setEncoding(Charset.forName("UTF-8"));
		textRecordSecretKey.setLocale(Locale.TAIWAN);
        message.add(textRecordSecretKey);		

        TextRecord textRecordCiphertext = new TextRecord();
        textRecordCiphertext.setText(ciphertext);
        textRecordCiphertext.setEncoding(Charset.forName("UTF-8"));
        textRecordCiphertext.setLocale(Locale.TAIWAN);
        message.add(textRecordCiphertext);   
	
		return message.getNdefMessage();
	}
	
    /**
     * Implementation for the OnNdefPushCompleteCallback interface
     */
    @Override
    public void onNdefPushComplete(NfcEvent arg0) {
        // A handler is needed to send messages to the activity when this
        // callback occurs, because it happens from a binder thread
        mHandler.obtainMessage(MESSAGE_SENT).sendToTarget();
    }

    /** This handler receives a message from onNdefPushComplete */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case MESSAGE_SENT:
                onNdefPushCompleteMessage();
                break;
            }
        }
    };
	
	/**
	 * 
	 * Implementation of {@link OnNdefPushCompleteCallback} interface.
	 * 
	 * This method is called after a successful transfer (push) of a message from this device to another.
	 */
	@Override
	protected void onNdefPushCompleteMessage() {
	    ToastMaker.toast(this, R.string.nfcBeamed);
		
		Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE) ;
		vibe.vibrate(500);	
		
        setResult(Activity.RESULT_OK);
        finish();
	}
	
    /**
     * 
     * NFC was found and enabled in settings, and push is enabled too.
     * 
     */
	@Override
	protected void onNfcPushStateEnabled() {
//	    ToastMaker.toast(this, R.string.nfcBeamAvailableEnabled);
	}

    /**
     * 
     * NFC was found and enabled in settings, but push is disabled
     * 
     */
	@Override
	protected void onNfcPushStateDisabled() {
	    ToastMaker.toast(this, R.string.nfcBeamAvailableDisabled);
        startNfcSettingsActivity();
        setResult(Activity.RESULT_CANCELED);
        finish();
	}

	/**
     * 
     * NFC beam setting changed since last check. For example, the user enabled beam in the wireless settings.
     * 
     */
	@Override
	protected void onNfcPushStateChange(boolean enabled) {
		if(enabled) {
		    ToastMaker.toast(this, R.string.nfcBeamAvailableEnabled);
		} else {
		    ToastMaker.toast(this, R.string.nfcBeamAvailableDisabled);
		}
	}

   /**
     * 
     * NFC feature was found and is currently enabled
     * 
     */
	@Override
	protected void onNfcStateEnabled() {
//	    ToastMaker.toast(this, R.string.nfcAvailableEnabled);
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
     * NFC setting changed since last check. For example, the user enabled NFC in the wireless settings.
     * 
     */
	
	@Override
	protected void onNfcStateChange(boolean enabled) {
		if(enabled) {
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
	}
	
	/**
	 * An NDEF message was read and parsed
	 * 
	 * @param message the message
	 */
	@Override
	protected void readNdefMessage(Message message) {
//		if(message.size() > 1) {
//		    ToastMaker.toast(this, R.string.readMultipleRecordNDEFMessage);
//		} else {
//		    ToastMaker.toast(this, R.string.readSingleRecordNDEFMessage);
//		}		
	}

	/**
	 * An empty NDEF message was read.
	 * 
	 */
	@Override
	protected void readEmptyNdefMessage() {
//	    ToastMaker.toast(this, R.string.readEmptyMessage);
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
//	    ToastMaker.toast(this, R.string.readNonNDEFMessage);
	}
	
}
