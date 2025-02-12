package com.example.urs_2024_25.nfc.cardemulation;


import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;

public class NfcCardEmulationService extends HostApduService {

    private static final String TAG = "HCEService";
    private static final long CUSTOM_ID = 123456701;  // Example custom ID (as an int)
    private static final String RESPONSE_OK = "9000"; // Status word for success

    @Override
    public byte[] processCommandApdu(byte[] apdu, Bundle extras) {
        Log.d(TAG, "Received APDU: " + bytesToHex(apdu));

        // Respond with the custom ID if it's a SELECT APDU
        if (Arrays.equals(hexToBytes("00A40400" + String.format("%02X",
                "F222222222".length() / 2) + "F222222222"), apdu)) {
            Log.d(TAG, "SELECT command received. Responding with custom ID.");

            Log.d(TAG, "Custom ID in Dec: " + CUSTOM_ID);
            // Convert int custom ID to hex and append the response status

            byte[] studentIdBytes = Long.toString(CUSTOM_ID).getBytes();

             Log.d(TAG, "Custom ID in bytes: " + studentIdBytes);
            return ConcatArrays(studentIdBytes, hexToBytes("9000")); // Send custom ID followed by success status
        } else {
            // Return failure if APDU command is unrecognized
            return hexToBytes("0000");
        }
    }

    public static byte[] ConcatArrays(byte[] first, byte[]... rest) {
        int totalLength = first.length;
        for (byte[] array : rest) {
            totalLength += array.length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (byte[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
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
        return 258424816; // Example student ID
    }
}


