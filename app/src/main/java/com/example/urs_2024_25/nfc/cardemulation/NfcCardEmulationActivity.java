package com.example.urs_2024_25.nfc.cardemulation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.urs_2024_25.R;

import android.os.Handler;
import android.view.View;

public class NfcCardEmulationActivity extends Activity {
    private Button emulateCardButton;
    private View dimOverlay;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_card_emulation);

        emulateCardButton = findViewById(R.id.emulateCardButton);
        dimOverlay = findViewById(R.id.dimOverlay);

        userId = getIntent().getIntExtra("USER_ID", -1);
        if (userId == -1) {
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        emulateCardButton.setOnClickListener(v -> {
            startCardEmulation();
            showButtonEffect();
        });
    }

    private void startCardEmulation() {
        Intent intent = new Intent(this, NfcCardEmulationService.class);
        intent.putExtra("USER_ID", userId);
        startService(intent);
    }

    private void showButtonEffect() {
        dimOverlay.setVisibility(View.VISIBLE);
        emulateCardButton.bringToFront();

        new Handler().postDelayed(() -> {
            dimOverlay.setVisibility(View.GONE);
        }, 5000);
    }
}

