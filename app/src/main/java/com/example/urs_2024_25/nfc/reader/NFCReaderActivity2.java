package com.example.urs_2024_25.nfc.reader;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.urs_2024_25.Attendance;
import com.example.urs_2024_25.R;
import com.example.urs_2024_25.liststudents.ListStudentsActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.Arrays;

public class NFCReaderActivity2 extends AppCompatActivity implements Attendance.AttendanceCallback, NfcAdapter.ReaderCallback {

    private static final String TAG = NFCReaderActivity2.class.getSimpleName();
    private TextView mTextViewExplanation, mTextViewStatus;
    private MainViewModel viewModel;
    private NfcAdapter nfcAdapter;
    private Attendance attendance;
    private static final int DEFAULT_CLASS_ID = 1;
    private static final byte[] SELECT_OK_SW = {(byte) 0x90, (byte) 0x00};

    String gotData = "", finalGotData = "";

    //onCreate() → onStart() → onResume() -> first launch
    //onResume() is always paired with onPause()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(TAG);
        Log.d(TAG, "onCreate()");

        setContentView(R.layout.activity_nfc_reader);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        attendance = new Attendance(db, this);

        // Initialize Views and ViewModel
        initViews();
        Log.d(TAG, "initviews()");

        // Initialize the List Students button to be clickable
        initListStudentsButton();


        Log.d(TAG, "initbutton()");

        // Initialize NFC adapter
        initNfcAdapter();


        Log.d(TAG, "initadapter()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableReaderMode();
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableReaderMode();
    }

    private void enableReaderMode() {
        if (nfcAdapter != null) {
            Bundle options = new Bundle();
            options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250);
            nfcAdapter.enableReaderMode(this, this,
                    NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, options);
            mTextViewStatus.setText("Hold NFC card near the device...");
        }
    }

    private void disableReaderMode() {
        if (nfcAdapter != null) {
            nfcAdapter.disableReaderMode(this);
        }
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        Log.d(TAG, "NFC Tag Discovered!");

        IsoDep isoDep = IsoDep.get(tag);
        if (isoDep != null) {
            try {
                isoDep.connect();

                // APDU SELECT command to get custom ID
                byte[] selectApdu = hexToBytes("00A40400" + String.format("%02X", "F222222222".length() / 2) + "F222222222");
                byte[] response = isoDep.transceive(selectApdu);

                int responseLength = response.length;
                byte[] statusWord = {response[responseLength - 2], response[responseLength - 1]};
                byte[] payload = Arrays.copyOf(response, responseLength - 2);

                if (Arrays.equals(SELECT_OK_SW, statusWord)) {
                    // The remote NFC device will immediately respond with its stored account number
                    String accountNumber = new String(payload, "UTF-8");
                    Log.i(TAG, "Received: " + accountNumber);
                    // Inform CardReaderFragment of received account number

                    int accountNumberInt = Integer.parseInt(accountNumber);
                    Log.i(TAG, "received int: " +accountNumberInt);


                        attendance.recordAttendance(DEFAULT_CLASS_ID, accountNumberInt);

                }
            } catch (IOException e) {
                Log.e(TAG, "Error communicating with card: " + e.toString());
            }
        }
    }


    // Convert hex string to byte array
    private byte[] hexToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    // Convert byte array to hex string
    private String bytesToHex(byte[] bytes) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private void initViews() {
        mTextViewExplanation = findViewById(R.id.text_view_explanation);
        mTextViewStatus = findViewById(R.id.text_view_status);
    }

    private void initNfcAdapter() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Log.e(TAG, "NFC is not supported on this device.");
            mTextViewStatus.setText("NFC not supported");
        }
    }

    private void initListStudentsButton() {
        Button buttonListStudents = findViewById(R.id.button_list_students);
        buttonListStudents.setClickable(true); // Ensure button is clickable
        buttonListStudents.setOnClickListener(v -> {
            Log.d(TAG, "Button List Students clicked!");
            startActivity(new Intent(NFCReaderActivity2.this, ListStudentsActivity.class));
        });
    }


    // AttendanceCallback implementation
    @Override
    public void onSuccess(String message) {
        runOnUiThread(() -> {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            // Optionally load and display updated attendance data
            attendance.loadAttendanceData();
        });
    }

    @Override
    public void onError(String message) {
        runOnUiThread(() -> {
            Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Attendance error: " + message);
        });
    }

    @Override
    public void onDataLoaded(String data) {
        runOnUiThread(() -> {
            // You can update a TextView or other UI element to show the attendance data
            // ivana: ovo je ono kad se displayaju svi skenovi
            mTextViewExplanation.setText(data);
        });
    }

}
