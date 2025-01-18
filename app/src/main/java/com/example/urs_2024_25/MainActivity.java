package com.example.urs_2024_25;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView mTextViewExplanation, mTextViewStatus;
    private MainViewModel viewModel;
    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");

        setContentView(R.layout.activity_main);

        // Initialize views
        mTextViewExplanation = findViewById(R.id.text_view_explanation);
        mTextViewStatus = findViewById(R.id.text_view_status);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Initialize NFC adapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Log.e(TAG, "NFC is not supported on this device.");
            mTextViewStatus.setText("NFC not supported");
            return;
        }

        // Observe NFC state changes
        viewModel.observeNFCState().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String nfcStatus) {
                mTextViewStatus.setText(nfcStatus);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");

        if (nfcAdapter == null) return;

        // Check and update NFC status
        viewModel.checkNFCStatus();

        // Enable foreground dispatch for NFC
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_MUTABLE
        );
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, viewModel.getIntentFilter(), viewModel.techList);

        // Handle NFC intent if available
        if (getIntent() != null) {
            onNewIntent(getIntent());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");

        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }

        viewModel.setNFC(NFCStatus.NoOperation);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent()");

        if (intent == null || intent.getAction() == null) {
            Log.e(TAG, "Received null intent or action.");
            return;
        }

        String action = intent.getAction();
        Log.d(ContentValues.TAG, "Intent Action: " + action);

        if (!viewModel.isTagDiscovered(action)) {
            Log.d(ContentValues.TAG, "Tag not discovered.");
            return;
        }

        // Process the NFC tag
        Parcelable tagN = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tagN != null) {
            Log.d(ContentValues.TAG, "Tag detected.");

            byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            String tagIdHex = viewModel.getByteArrayToHexString(id);

            mTextViewExplanation.setText(tagIdHex);
            viewModel.setNFC(NFCStatus.Read);

            String tagData = viewModel.dumpTagData(tagN);
            String formattedData = viewModel.getDateTimeNow(tagData);
            mTextViewExplanation.setText(formattedData);

            createNdefMessage(tagData, id);
        } else {
            Log.d(ContentValues.TAG, "No tag found in intent.");
        }

        // Check for NDEF messages
        Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMessages != null) {
            Log.d(ContentValues.TAG, "Found " + rawMessages.length + " NDEF messages.");
        } else {
            Log.d(ContentValues.TAG, "No NDEF messages found.");
        }
    }

    private void createNdefMessage(String data, byte[] id) {
        if (data == null || id == null) {
            Log.e(TAG, "Invalid data or tag ID for NDEF message creation.");
            return;
        }

        try {
            byte[] payload = data.getBytes(StandardCharsets.UTF_8);
            NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, new byte[0], id, payload);
            NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{record});

            Log.d(TAG, "Created NDEF Message: " + Arrays.toString(ndefMessage.getRecords()));
        } catch (Exception e) {
            Log.e(TAG, "Error creating NDEF message: " + e.getMessage(), e);
        }
    }
}
