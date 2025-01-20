package com.example.urs_2024_25;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView mTextViewExplanation, mTextViewStatus;
    private MainViewModel viewModel;
    private NfcAdapter nfcAdapter;

    //onCreate() → onStart() → onResume() -> first launch
    //onResume() is always paired with onPause()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");

        setContentView(R.layout.activity_main);

        // Initialize Views and ViewModel
        initViews();
        initViewModel();

        // Initialize NFC adapter
        initNfcAdapter();

        // Observe NFC state changes
        viewModel.observeNFCState().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String nfcStatus) {
                Log.e(nfcStatus, "nfcStatus check");
                mTextViewStatus.setText(nfcStatus); // update of text displaying the state
            }
        });
    }

    private void initViews() {
        mTextViewExplanation = findViewById(R.id.text_view_explanation);
        mTextViewStatus = findViewById(R.id.text_view_status);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.observeNFCState().observe(this, this::updateNfcStatus);
    }

    private void updateNfcStatus(String nfcStatus) {
        Log.e(TAG, "NFC Status: " + nfcStatus);
        mTextViewStatus.setText(nfcStatus);
    }

    private void initNfcAdapter() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Log.e(TAG, "NFC is not supported on this device.");
            mTextViewStatus.setText("NFC not supported");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");

        if (nfcAdapter == null) return;

        // Check NFC status and enable foreground dispatch
        viewModel.checkNFCStatus();
        enableForegroundDispatch();

        // Handle NFC intent if available
        if (getIntent() != null) {
            onNewIntent(getIntent());
        }
    }

    private void enableForegroundDispatch() {
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_MUTABLE
        );
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, viewModel.getIntentFilter(), viewModel.techList);
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

        handleNfcTag(intent);
        handleNdefMessages(intent);
    }

    private void handleNfcTag(Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "Intent Action: " + action);

        if (!viewModel.isTagDiscovered(action)) {
            Log.d(TAG, "Tag not discovered.");
            return;
        }

        Parcelable tagN = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tagN != null) {
            processTagData(tagN, intent);
        } else {
            Log.d(TAG, "No tag found in intent.");
        }
    }

    @SuppressLint("SetTextI18n")
    private void processTagData(Parcelable tagN, Intent intent) {
        byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
        String tagIdHex = viewModel.getByteArrayToHexString(id);
        long tagIdDec = getDec(id);

        mTextViewExplanation.setText(tagIdHex);
        viewModel.setNFC(NFCStatus.Read);

        String tagData = viewModel.dumpTagData(tagN); //data returned from tag;
        //String formattedData = viewModel.getDateTimeNow(tagData);
        //Log.d(TAG, "Formatted Data: " + formattedData);
        Log.d(TAG, "Decimal id: " + tagIdDec);
        NfcRecord nfcRecord = new NfcRecord();
        nfcRecord.setNfcId(tagIdDec);
        nfcRecord.setTimestamp(getCurrentTimestamp());

        //save nfcRecord to Db -> TODO
        mTextViewExplanation.setText("Data from tag: \n" + tagData + "\n\nProcessed data: \n"
                +  "Card ID(dec):" + nfcRecord.getNfcId() + "\nTimeStamp:" + nfcRecord.getTimeStamp());


        //createNdefMessage(tagData, id);
    }

    private LocalDateTime getCurrentTimestamp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return LocalDateTime.now();
        }
        return null;
    }

    private void handleNdefMessages(Intent intent) {
        Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMessages != null) {
            Log.d(TAG, "Found " + rawMessages.length + " NDEF messages.");
        } else {
            Log.d(TAG, "No NDEF messages found.");
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

    private long getDec(final byte[] bytes) {
        Log.d(TAG, "getDec()");
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffL;
            result += value * factor;
            factor *= 256L;
        }
        return result;
    }
}
