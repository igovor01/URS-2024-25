package com.example.urs_2024_25.nfc.cardemulation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.urs_2024_25.R;

public class NfcCardEmulationActivity extends Activity {

    private static final String TAG = "HCEActivity";
    private Button emulateCardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_card_emulation);

        emulateCardButton = findViewById(R.id.emulateCardButton);

        // Set the button click listener
        emulateCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the service to emulate the NFC card
                startCardEmulation();
            }
        });
    }

    private void startCardEmulation() {
        // Activate the NFC card emulation via HostApduService
        Log.d(TAG, "STAAAAART");
        Intent intent = new Intent(NfcCardEmulationActivity.this, NfcCardEmulationService.class);
        startService(intent);
        Log.d(TAG, "STAAAAART end");
    }
}