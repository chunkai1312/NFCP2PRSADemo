package edu.ntust.cs.idsl.nfcp2prsademo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 * 
 */
public class BeamWriterFragment extends Fragment {

    private EditText editTextPlaintext;
    private TextView textViewCiphertext;
    private TextView textViewEncryptedSecretKey;
    private Button buttonEncryptPlaintext;
    private Button buttonEncryptSecretKey;
    private Button buttonBeam;
    private MyApplication app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MyApplication) getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_beam_writer, container, false);

        editTextPlaintext = (EditText) rootView.findViewById(R.id.editTextPlaintext);
        textViewCiphertext = (TextView) rootView.findViewById(R.id.textViewCiphertext);
        textViewEncryptedSecretKey = (TextView) rootView.findViewById(R.id.textViewEncryptedSecretKey);
        
        buttonEncryptPlaintext = (Button) rootView.findViewById(R.id.buttonEncryptPlaintext);
        buttonEncryptPlaintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {    
                if (app.getSettings().getSecretKey().isEmpty()) {
                    ToastMaker.toast(getActivity(), R.string.please_generate_your_key);
                    return;
                }
                if (editTextPlaintext.getText().toString().isEmpty()) {
                    ToastMaker.toast(getActivity(), R.string.plaintext_empty);
                    return;
                }
                try {
                    byte[] secretKey = AESCoder.base64(app.getSettings().getSecretKey());
                    String plaintext = editTextPlaintext.getText().toString();
                    String ciphertext = AESCoder.encrypt(plaintext, secretKey);
                    textViewCiphertext.setText(ciphertext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        
        buttonEncryptSecretKey = (Button) rootView.findViewById(R.id.buttonEncryptSecretKey);
        buttonEncryptSecretKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   
                if (app.getSettings().getSecretKey().toString().isEmpty()) {
                    ToastMaker.toast(getActivity(), R.string.please_generate_your_key);
                    return;
                }
                try {
                    byte[] publicKey = RSACoder.base64(app.getSettings().getPublicKey());
                    String secretKey = RSACoder.base64(RSACoder.encryptByPublicKey(RSACoder.base64(app.getSettings().getSecretKey()), publicKey));
                    textViewEncryptedSecretKey.setText(secretKey);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        
        buttonBeam = (Button) rootView.findViewById(R.id.buttonBeam);
        buttonBeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {    
                String encryptedSecretKey = textViewEncryptedSecretKey.getText().toString();
                String ciphertext = textViewCiphertext.getText().toString();
                
                if (encryptedSecretKey.isEmpty()) {
                    ToastMaker.toast(getActivity(), R.string.encrypted_secret_key_empty);
                    return;
                }
                
                if (ciphertext.isEmpty()) {
                    ToastMaker.toast(getActivity(), R.string.ciphertext_empty);
                    return;
                }
                
                startActivityForResult(BeamWriterActivity.getIntent(getActivity(), encryptedSecretKey, ciphertext), 0);
            }
        });

        return rootView;
    }

}
