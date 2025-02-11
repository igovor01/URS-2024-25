package com.example.urs_2024_25.nfc.cardemulation;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

public class NfcCardEmulationService extends HostApduService {

    private static final String TAG = "NfcCardEmulationService";
    private static final String RESPONSE_OK = "9000"; // Status word for success
    private static final String RESPONSE_ERROR = "6F00"; // Status word for failure


    //when NFC reader scans a student's NFC-enabled phone this method is called
    //phone must respond accordingly
    //if the command is a SELECT APDU, it sends back a custom student ID
    //if the command is unknown, it sends an error response
    @Override
    public byte[] processCommandApdu(byte[] apdu, Bundle extras) {
        if (apdu == null) {
            Log.e(TAG, "Received null APDU");
            return hexToBytes(RESPONSE_ERROR);
        }

        Log.d(TAG, "Received APDU: " + bytesToHex(apdu));

        // Check if it's a SELECT APDU command
        if (isSelectApdu(apdu)) {
            Log.d(TAG, "SELECT command received. Responding with custom ID.");

            // Fetch the custom ID (e.g., from a database or shared preferences)
            long customId = getStudentIdFromDatabase();
            Log.d(TAG, "Custom ID in Dec: " + customId);

            // Convert custom ID to a fixed-length hex string (e.g., 8 bytes)
            String customIdHex = String.format("%08X", customId); // Pad with leading zeros
            Log.d(TAG, "Custom ID in Hex: " + customIdHex);

            // Append the success status and return the response
            return hexToBytes(customIdHex + RESPONSE_OK);
        } else {
            // Return an error for unrecognized commands
            Log.e(TAG, "Unrecognized APDU command");
            return hexToBytes(RESPONSE_ERROR);
        }
    }

    @Override
    public void onDeactivated(int reason) {
        Log.d(TAG, "Card deactivated. Reason: " + reason);
    }

    // Check if the APDU is a SELECT command
    private boolean isSelectApdu(byte[] apdu) {
        return apdu.length >= 2 && apdu[0] == (byte) 0x00 && apdu[1] == (byte) 0xA4;
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

    // Fetch the student ID from a database or shared preferences
    private long getStudentIdFromDatabase() {
        // Replace this with actual logic to fetch the student ID
        return 258424815; // Example student ID
    }
}