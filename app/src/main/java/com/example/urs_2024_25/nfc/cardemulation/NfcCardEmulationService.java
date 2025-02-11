package com.example.urs_2024_25.nfc.cardemulation;


import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

public class NfcCardEmulationService extends HostApduService {

    private static final String TAG = "HCEService";
    private static final long CUSTOM_ID = 258424815;  // Example custom ID (as an int)
    private static final String RESPONSE_OK = "9000"; // Status word for success

    @Override
    public byte[] processCommandApdu(byte[] apdu, Bundle extras) {
        Log.d(TAG, "Received APDU: " + bytesToHex(apdu));

        // Respond with the custom ID if it's a SELECT APDU
        if (isSelectApdu(apdu)) {
            Log.d(TAG, "SELECT command received. Responding with custom ID.");

            Log.d(TAG, "Custom ID in Dec: " + CUSTOM_ID);
            // Convert int custom ID to hex and append the response status
            String customIdHex = Long.toHexString(CUSTOM_ID).toUpperCase();

            Log.d(TAG, "Custom ID in Hex: " + customIdHex);
            return hexToBytes(customIdHex + RESPONSE_OK); // Send custom ID followed by success status
        } else {
            // Return failure if APDU command is unrecognized
            return hexToBytes(RESPONSE_OK);
        }
    }

    @Override
    public void onDeactivated(int reason) {
        // Handle deactivation
        Log.d(TAG, "Card deactivated. Reason: " + reason);
    }

    // Utility method to check if it's a SELECT command
    private boolean isSelectApdu(byte[] apdu) {
        return apdu != null && apdu.length > 0 && apdu[0] == (byte) 0x00 && apdu[1] == (byte) 0xA4;
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

    // Convert byte array to hex string (for logging)
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
    // Replace this with the actual logic to fetch the logged-in student's ID
    private long getStudentIdFromDatabase() {
        // Simulate fetching the student ID
        // In practice, you would query your database or use an API call
        return 258424815; // Example student ID
    }
}


