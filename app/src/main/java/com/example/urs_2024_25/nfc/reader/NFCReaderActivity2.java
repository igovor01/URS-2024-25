package com.example.urs_2024_25.nfc.reader;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.nfc.tech.MifareClassic;
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

public class NFCReaderActivity2 extends AppCompatActivity implements Attendance.AttendanceCallback, NfcAdapter.ReaderCallback {

    private static final String TAG = NFCReaderActivity2.class.getSimpleName();
    private TextView mTextViewExplanation, mTextViewStatus;
    private NfcAdapter nfcAdapter;
    private Attendance attendance;
    private static final int DEFAULT_CLASS_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_reader);

        // Initialize Firebase Firestore and Attendance
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        attendance = new Attendance(db, this);

        // Initialize Views
        initViews();

        // Initialize the List Students button
        initListStudentsButton();

        // Initialize NFC adapter
        initNfcAdapter();
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
                    NfcAdapter.FLAG_READER_NFC_A |
                            NfcAdapter.FLAG_READER_NFC_B |
                            NfcAdapter.FLAG_READER_NFC_F |
                            NfcAdapter.FLAG_READER_NFC_V |
                            NfcAdapter.FLAG_READER_NFC_BARCODE |
                            NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, options);
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

        // Check for supported NFC technologies
        if (IsoDep.get(tag) != null) {
            handleIsoDepTag(tag);
        } else if (NfcA.get(tag) != null) {
            handleNfcATag(tag);
        } else if (NfcB.get(tag) != null) {
            handleNfcBTag(tag);
        } else if (NfcF.get(tag) != null) {
            handleNfcFTag(tag);
        } else if (NfcV.get(tag) != null) {
            handleNfcVTag(tag);
        } else if (MifareClassic.get(tag) != null) {
            handleMifareClassicTag(tag);
        } else {
            Log.e(TAG, "Unsupported NFC tag type.");
            runOnUiThread(() -> Toast.makeText(this, "Unsupported NFC tag type", Toast.LENGTH_SHORT).show());
        }
    }

    private void handleIsoDepTag(Tag tag) {
        IsoDep isoDep = IsoDep.get(tag);
        if (isoDep != null) {
            try {
                isoDep.connect();

                // APDU SELECT command to get custom ID
                byte[] selectApdu = {
                        (byte) 0x00, (byte) 0xA4,  // CLA + INS
                        (byte) 0x04, 0x00,          // P1 + P2
                        (byte) 0x07,                // Length of AID (7 bytes)
                        (byte) 0xF0, (byte) 0x01, (byte) 0x23, (byte) 0x45, (byte) 0x67, (byte) 0x89 // AID
                };
                byte[] response = isoDep.transceive(selectApdu);

                if (response != null) {
                    String responseHex = bytesToHex(response);
                    String customIdHex = responseHex.substring(0, responseHex.length() - 4); // Remove "9000"

                    Log.d(TAG, "Received Response ID Hex (as hex): " + responseHex);
                    Log.d(TAG, "Received Custom ID (as hex): " + customIdHex);

                    // Convert HEX string to long
                    long customId = Long.parseLong(customIdHex, 16);
                    Log.d(TAG, "Received Custom ID (as long): " + customId);

                    runOnUiThread(() -> mTextViewExplanation.setText("Custom ID: " + customId));

                    // Record attendance
                    attendance.recordAttendance(DEFAULT_CLASS_ID, (int) customId);
                } else {
                    Log.e(TAG, "No response from card.");
                    runOnUiThread(() -> Toast.makeText(this, "No response from card", Toast.LENGTH_SHORT).show());
                }

                isoDep.close();
            } catch (IOException | NumberFormatException e) {
                Log.e(TAG, "Error processing NFC tag", e);
                runOnUiThread(() -> Toast.makeText(this, "Error processing NFC tag", Toast.LENGTH_SHORT).show());
            }
        }
    }

    private void handleNfcATag(Tag tag) {
        // Handle NFC-A tags (e.g., Mifare Ultralight)
        Log.d(TAG, "NFC-A tag detected.");
        runOnUiThread(() -> Toast.makeText(this, "NFC-A tag detected", Toast.LENGTH_SHORT).show());
    }

    private void handleNfcBTag(Tag tag) {
        // Handle NFC-B tags
        Log.d(TAG, "NFC-B tag detected.");
        runOnUiThread(() -> Toast.makeText(this, "NFC-B tag detected", Toast.LENGTH_SHORT).show());
    }

    private void handleNfcFTag(Tag tag) {
        // Handle NFC-F tags (e.g., Felica)
        Log.d(TAG, "NFC-F tag detected.");
        runOnUiThread(() -> Toast.makeText(this, "NFC-F tag detected", Toast.LENGTH_SHORT).show());
    }

    private void handleNfcVTag(Tag tag) {
        // Handle NFC-V tags (e.g., ISO 15693)
        Log.d(TAG, "NFC-V tag detected.");
        runOnUiThread(() -> Toast.makeText(this, "NFC-V tag detected", Toast.LENGTH_SHORT).show());
    }

    private void handleMifareClassicTag(Tag tag) {
        // Handle Mifare Classic tags
        Log.d(TAG, "Mifare Classic tag detected.");
        runOnUiThread(() -> Toast.makeText(this, "Mifare Classic tag detected", Toast.LENGTH_SHORT).show());
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
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
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
            mTextViewExplanation.setText(data);
        });
    }
}