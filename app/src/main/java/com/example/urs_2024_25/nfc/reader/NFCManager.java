package com.example.urs_2024_25.nfc.reader;

import android.app.Activity;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class NFCManager {

    private static final String TAG = NFCManager.class.getSimpleName();

    // Enable NFC Reader Mode
    @RequiresApi(api = Build.VERSION_CODES.KITKAT) //makes sure that method can only be used on devices running at least a specific API level
    public static void enableReaderMode(Context context, Activity activity, NfcAdapter.ReaderCallback callback, int flags, Bundle extras) {
        try {
            NfcAdapter.getDefaultAdapter(context).enableReaderMode(activity, callback, flags, extras);
        } catch (UnsupportedOperationException ex) {
            Log.e(TAG, "UnsupportedOperationException " + ex.getMessage(), ex);
        }
    }

    // Disable NFC Reader Mode
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void disableReaderMode(Context context, Activity activity) {
        try {
            NfcAdapter.getDefaultAdapter(context).disableReaderMode(activity);
        } catch (UnsupportedOperationException ex) {
            Log.e(TAG, "UnsupportedOperationException " + ex.getMessage(), ex);
        }
    }

    // Check if NFC is supported
    public static Boolean isSupported(final Context context) {
        @Nullable
        final NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        return nfcAdapter != null;
    }

    // Check if NFC is not supported
    public static Boolean isNotSupported(final Context context) {
        @Nullable
        final NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        return nfcAdapter == null;
    }

    // Check if NFC is enabled
    public static Boolean isEnabled(final Context context) {
        @Nullable
        final NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        return nfcAdapter != null && nfcAdapter.isEnabled();
    }

    // Check if NFC is not enabled
    public static Boolean isNotEnabled(final Context context) {
        @Nullable
        final NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        return nfcAdapter == null || !nfcAdapter.isEnabled();
    }

    // Check if NFC is supported and enabled
    public static Boolean isSupportedAndEnabled(final Context context) {
        return isSupported(context) && isEnabled(context);
    }
}
