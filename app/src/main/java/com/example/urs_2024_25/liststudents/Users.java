package com.example.urs_2024_25.liststudents;
import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Users {
    private static final String COLLECTION_NAME = "Students";

    private static final String FIELD_USER_ID = "userID";
    private static final String FIELD_NAME = "name";

    private static final String FIELD_SURNAME = "surname";

    public interface DataCallback {
        void onDataLoaded(List<UserModel> usersData);
        void onError(String errorMessage);
    }



    //List of users saved in db -> not used
    public void loadUsersFromDb(DataCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(COLLECTION_NAME)
                .orderBy(FIELD_SURNAME, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<UserModel> usersData = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Long userId = document.getLong(FIELD_USER_ID);
                            String name = document.getString(FIELD_NAME);
                            String surname = document.getString(FIELD_SURNAME);

                            // Only add valid users
                            if (userId != null && name != null && surname != null) {
                                UserModel user = new UserModel();
                                user.setUserID(userId);
                                user.setName(name);
                                user.setSurname(surname);
                                usersData.add(user);
                            } else {
                                Log.w(TAG, "Skipping user with missing fields: " + document.getId());
                            }
                        }
                        Log.d(TAG, "Loaded User Data: " + usersData.size());
                        callback.onDataLoaded(usersData);
                    } else {
                        Log.e(TAG, "Error loading user data", task.getException());
                        callback.onError("Error loading student data: " + task.getException().getMessage());
                    }
                });
    }

   public void loadStudentsBasedOnAttendance(DataCallback callback, final Long classId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Query the Attendance collection for the given class and professor
        db.collection("Attendance")
                .whereEqualTo("class_id", classId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        List<NfcRecord> users = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Long userId = document.getLong("user_id");
                            if (userId != null) {
                                NfcRecord student = new NfcRecord();
                                student.setNfcId(userId);
                                student.setTimestamp(document.getString("timestamp"));
                                users.add(student);
                            }
                        }
                        fetchStudentsDetails(callback, users);
                    } else {
                        Log.e("AttendanceQuery", "Error loading attendance data");
                    }
                });
    }

    private void fetchStudentsDetails(DataCallback callback, List<NfcRecord> users) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (users.isEmpty()) {
            Log.i("StudentQuery", "No students found for the given attendance.");
            return;
        }

        List<Long> userIds = users.stream()
                .map(NfcRecord::getNfcId)
                .collect(Collectors.toList());

        // Query the Students collection for matching user IDs
        db.collection(COLLECTION_NAME)
                .whereIn("userID", userIds )
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<UserModel> students = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            UserModel user = new UserModel();
                            user.setUserID(document.getLong("userID"));
                            user.setName(document.getString("name"));
                            user.setSurname(document.getString("surname"));
                            String timeStamp = users.stream()
                                    .filter(student -> student.getNfcId().equals(document.getLong("userID")))
                                    .map(NfcRecord::getTimeStamp)
                                    .findFirst()
                                    .orElse(null);
                            user.setTimeStamp(timeStamp);
                            students.add(user);
                        }

                        callback.onDataLoaded(students);
                    } else {
                        Log.e("StudentQuery", "Error fetching student details");
                        callback.onError("Error fetching student details");
                    }
                });
    }

}
