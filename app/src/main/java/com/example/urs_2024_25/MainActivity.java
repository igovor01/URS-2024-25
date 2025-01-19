package com.example.urs_2024_25;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements Attendance.AttendanceCallback {
    private static final String TAG = "MainActivity";
    private TextView dataTextView;
    private Attendance attendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataTextView = findViewById(R.id.dataTextView);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        attendance = new Attendance(db, this);

        // Example of recording attendance
        attendance.recordAttendance(999, 201);

        // Load and display attendance records
        attendance.loadAttendanceData();
    }

    @Override
    public void onSuccess(String message) {
        Log.d(TAG, message);
        dataTextView.append(message + "\n");
    }

    @Override
    public void onError(String message) {
        Log.w(TAG, message);
        dataTextView.append(message + "\n");
    }

    @Override
    public void onDataLoaded(String data) {
        dataTextView.append(data);
    }
}