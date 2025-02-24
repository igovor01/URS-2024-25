package com.example.urs_2024_25.nfc.cardreader;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

public class NfcCardReaderService extends Service {

    private static final String TAG = NfcCardReaderService.class.getSimpleName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
