package com.example.urs_2024_25;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Attendance {
    private static final String TAG = "Attendance";
    private static final String COLLECTION_NAME = "Attendance";
    private static final String FIELD_CLASS_ID = "class_id";
    private static final String FIELD_USER_ID = "user_id";
    private static final String FIELD_TIMESTAMP = "timestamp";

    private final FirebaseFirestore db;
    private final AttendanceCallback callback;

    public interface AttendanceCallback {
        void onSuccess(String message);

        void onError(String message);

        void onDataLoaded(String data);
    }

    public Attendance(FirebaseFirestore db, AttendanceCallback callback) {
        this.db = db;
        this.callback = callback;
    }

    public void recordAttendance(final int classId, final int userId) {
        Log.i(TAG,"ATART RECORDING");

        db.collection(COLLECTION_NAME)
                .whereEqualTo(FIELD_CLASS_ID, classId)
                .whereEqualTo(FIELD_USER_ID, userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        Log.i(TAG,"attendence classid "+classId+ " useerid "+ userId);
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                Log.i(TAG,"before ");

                                addNewAttendanceRecord(classId, userId);
                                Log.i(TAG,"after ");
                            } else {
                                Log.i(TAG,"attendence classid "+classId+ " useerid "+ userId);

                                callback.onError("Attendance already recorded for this class and user");
                            }
                        } else {
                            callback.onError("Error checking existing attendance");
                        }
                    }
                });
    }

    private void addNewAttendanceRecord(int classId, int userId) {
        Log.i(TAG,"here1");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        Log.i(TAG,"here2");
        String timestamp = sdf.format(new Date());
        Log.i(TAG,"addnew classid "+classId+ " useerid "+ userId);


        Map<String, Object> attendance = new HashMap<>();
        attendance.put(FIELD_CLASS_ID, classId);
        Log.i(TAG,"here3");
        attendance.put(FIELD_USER_ID, userId);
        Log.i(TAG,"here4");
        attendance.put(FIELD_TIMESTAMP, timestamp);
        Log.i(TAG,"here5");

        db.collection(COLLECTION_NAME)
                .add(attendance)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i(TAG,"here success");
                        callback.onSuccess("Attendance recorded successfully");
                        Log.i(TAG,"here after success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG,"here fail");
                        Log.e(TAG, "Error recording attendance");
                        callback.onError("Error recording attendance");
                    }
                });
    }

    public void loadAttendanceData() {
        db.collection(COLLECTION_NAME)
                .orderBy(FIELD_TIMESTAMP, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            StringBuilder data = new StringBuilder();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                data.append("Record ID: ").append(document.getId())
                                        .append("\nClass ID: ").append(document.getLong(FIELD_CLASS_ID))
                                        .append("\nUser ID: ").append(document.getLong(FIELD_USER_ID))
                                        .append("\nTimestamp: ").append(document.getString(FIELD_TIMESTAMP))
                                        .append("\n\n");
                            }
                            callback.onDataLoaded(data.toString());
                        } else {
                            callback.onError("Error loading attendance data");
                        }
                    }
                });
    }
}