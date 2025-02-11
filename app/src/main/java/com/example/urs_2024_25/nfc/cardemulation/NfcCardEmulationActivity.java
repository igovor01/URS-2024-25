package com.example.urs_2024_25.nfc.cardemulation;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.urs_2024_25.R;

public class NfcCardEmulationActivity extends Activity {

    private static final String TAG = "NfcCardEmulationActivity";
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
                // Provide feedback to the user
                Toast.makeText(NfcCardEmulationActivity.this, "NFC card emulation is active", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "NFC card emulation is active");
            }
        });
    }
}