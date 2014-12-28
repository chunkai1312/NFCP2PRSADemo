package edu.ntust.cs.idsl.nfcp2prsademo;

import org.ndeftools.Message;
import org.ndeftools.wellknown.TextRecord;

import android.app.Activity;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 * 
 */
public class BeamReaderFragment extends Fragment {

    private static final String EXTRA = "edu.ntust.cs.idsl.nfctagdemo.extra.MESSAGE";
    private ListView listViewContent;
    private TextView textViewEncryptedSecretKey;
    private TextView textViewSecretKey;
    private TextView textViewCiphertext;
    private TextView textViewPlaintext;
    private Button buttonDecryptEncryptedSecretKey;
    private Button buttonDecryptCiphertext;
    private Button buttonBeam;
    private MyApplication app;

    public static Intent getIntentForResult(Message message) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA, message.getNdefMessage());
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MyApplication) getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_beam_reader, container, false);

        listViewContent = (ListView) rootView.findViewById(R.id.listViewContent);
        textViewEncryptedSecretKey = (TextView) rootView.findViewById(R.id.textViewEncryptedSecretKey);
        textViewSecretKey = (TextView) rootView.findViewById(R.id.textViewSecretKey);
        textViewCiphertext = (TextView) rootView.findViewById(R.id.textViewCiphertext);
        textViewPlaintext = (TextView) rootView.findViewById(R.id.textViewPlaintext);
        
        buttonDecryptEncryptedSecretKey = (Button) rootView.findViewById(R.id.buttonDecryptEncryptedSecretKey);
        buttonDecryptEncryptedSecretKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textViewEncryptedSecretKey.getText().toString().isEmpty()) {
                    ToastMaker.toast(getActivity(), R.string.encrypted_secret_key_empty);
                    return;
                }
                try {
                    byte[] privateKey = RSACoder.base64(app.getSettings().getPrivateKey());
                    String encryptedSecretKey = textViewEncryptedSecretKey.getText().toString();
                    String secretKey = RSACoder.base64(RSACoder.decryptByPrivateKey(RSACoder.base64(encryptedSecretKey), privateKey));
                    textViewSecretKey.setText(secretKey);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        
        buttonDecryptCiphertext = (Button) rootView.findViewById(R.id.buttonDecryptCiphertext);
        buttonDecryptCiphertext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textViewCiphertext.getText().toString().isEmpty()) {
                    ToastMaker.toast(getActivity(), R.string.ciphertext_empty);
                    return;
                }
                try {
                    byte[] secretKey = AESCoder.base64(textViewSecretKey.getText().toString());
                    String ciphertext = textViewCiphertext.getText().toString();
                    String plaintext = AESCoder.decrypt(ciphertext, secretKey);
                    textViewPlaintext.setText(plaintext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        
        buttonBeam = (Button) rootView.findViewById(R.id.buttonBeam);
        buttonBeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(BeamReaderActivity.getIntent(getActivity()), 0);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (Activity.RESULT_OK == resultCode) {
            NdefMessage ndefMessage = (NdefMessage) data.getParcelableExtra(EXTRA);

            try {
                Message message = new Message(ndefMessage);
                showContent(message);
            } catch (FormatException e) {
                e.printStackTrace();
            }
        }

        if (Activity.RESULT_CANCELED == resultCode) {
            clearContent();
        }
    }

    private void showContent(Message message) {
        if (message != null && !message.isEmpty()) {
            ArrayAdapter<? extends Object> adapter = new NdefRecordAdapter(getActivity(), message);
            listViewContent.setAdapter(adapter);
        } else {
            clearContent();
        }

        if (message.get(0) instanceof TextRecord) {
            TextRecord textRecordEncryptedSecretKey = (TextRecord) message.get(0);
            textViewEncryptedSecretKey.setText(textRecordEncryptedSecretKey.getText());
            textViewSecretKey.setText("");
        }
        
        if (message.get(1) instanceof TextRecord) {
            TextRecord textRecordCiphertext = (TextRecord) message.get(1);
            textViewCiphertext.setText(textRecordCiphertext.getText());
            textViewPlaintext.setText("");
        }
    }

    private void clearContent() {
        listViewContent.setAdapter(null);
        textViewEncryptedSecretKey.setText("");
        textViewSecretKey.setText("");
        textViewCiphertext.setText("");
        textViewPlaintext.setText("");
    }

}
