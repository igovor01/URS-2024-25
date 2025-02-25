package com.example.urs_2024_25.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.urs_2024_25.R;
import com.example.urs_2024_25.nfc.cardemulation.NfcCardEmulationActivity;
import com.example.urs_2024_25.signup.SignUpStudentActivity;

public class LogInStudentActivity extends AppCompatActivity {

    private EditText loginEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in_student);

        loginEmail = findViewById(R.id.login_student_email);
        TextView signupRedirectText = findViewById(R.id.signup_student_redirect);
        Button loginButton = findViewById(R.id.login_student_button);

        loginButton.setOnClickListener(v -> {
            String email = loginEmail.getText().toString();

            if (email.isEmpty()) {
                loginEmail.setError("Email cannot be empty");
                return;
            }

            int userId = getUserIdByEmail(email);
            if (userId == -1) {
                loginEmail.setError("Invalid email");
                return;
            }

            Intent intent = new Intent(LogInStudentActivity.this, NfcCardEmulationActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        signupRedirectText.setOnClickListener(v ->
                startActivity(new Intent(LogInStudentActivity.this, SignUpStudentActivity.class))
        );
    }

    private int getUserIdByEmail(String email) {
        switch (email) {
            case "ela": return 258424815;
            case "karla": return 257279471;
            case "ivana": return 258793359;
            case "marija": return 258765135;
            default: return -1;
        }
    }

//    private int getUserID(String nameInput){
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("users")
//                .whereEqualTo("name", nameInput)
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    if (!queryDocumentSnapshots.isEmpty()) {
//                        // Get the first matching document
//                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
//
//                        // Extract the userId
//                        String userId = document.getString("userId");
//                        String surname = document.getString("surname");
//
//                        // Now you have the userId
//                        Log.d("FIRESTORE", "Found userId: " + userId + " for user: " + nameInput + " " + surname);
//
//                        assert userId != null;
//                        userIdToEmulate = Integer.parseInt(userId);
//                        // Do something with the userId
//                        // For example, store it or use it for further queries
//                    } else {
//                        Log.d("FIRESTORE", "No user found with name: " + nameInput);
//                        // Show message to user that no matching user was found
//                        userIdToEmulate=-1;
//                        Toast.makeText(LogInStudentActivity.this, "User not found", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    Log.e("FIRESTORE", "Error searching for user", e);
//                    userIdToEmulate=-1;
//                    Toast.makeText(LogInStudentActivity.this, "Error searching for user", Toast.LENGTH_SHORT).show();
//                });
//        return userIdToEmulate;
//    }
//    private int getUserID(String name) {
//        List<UserModel> usersDataList = new ArrayList<>();
//        Users users = new Users();
//        users.loadUsersFromDb(new Users.DataCallback() {
//            @Override
//            public void onDataLoaded(List<UserModel> usersData) {
//                if (!usersData.isEmpty()) {
//                    Log.i("IVANA", "usersData" + usersData);
//                    usersDataList.addAll(usersData);
//                    Log.i("IVANA", "usersDataList" + usersDataList);
//                }
//            }
//            @Override
//            public void onError(String errorMessage) {
//                Toast.makeText(LogInStudentActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        var userY = usersDataList.stream()
//                .filter(user -> user.getName().equalsIgnoreCase(name))
//                .findFirst()
//                .orElse(null);
//        return userY != null ? (int)userY.getUserID() : -1;
//    }

}
