package com.example.urs_2024_25.signup;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Users2 {
    private static final String COLLECTION_NAME = "Students";
    private static final String FIELD_USER_ID = "userID";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_SURNAME = "surname";

    private final FirebaseFirestore db;
    private final Users2.DataCallback callback;

    public interface DataCallback {
        void onSuccess(String message);
        void onError(String message);
    }

    public Users2(FirebaseFirestore db, Users2.DataCallback callback) {
        this.db = db;
        this.callback = callback;
    }

    public void recordUser(String name, String surname, final int userId) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo(FIELD_USER_ID, userId)
                .whereEqualTo(FIELD_NAME, name)
                .whereEqualTo(FIELD_SURNAME, surname)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            addNewUserRecord(name, surname, userId);
                        } else {
                            callback.onError("User already recorded");
                        }
                    } else {
                        callback.onError("Error checking existing user");
                    }
                });
    }

    private void addNewUserRecord(String name, String surname, int userId) {
        Map<String, Object> user = new HashMap<>();
        user.put(FIELD_USER_ID, userId);
        user.put(FIELD_NAME, name);
        user.put(FIELD_SURNAME, surname);

        db.collection(COLLECTION_NAME)
                .add(user)
                .addOnSuccessListener(documentReference -> callback.onSuccess("User recorded successfully"))
                .addOnFailureListener(e -> callback.onError("Error recording user"));
    }
}
