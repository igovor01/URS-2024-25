package com.example.urs_2024_25.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.urs_2024_25.R;
import com.example.urs_2024_25.nfc.cardemulation.NfcCardEmulationActivity;
import com.example.urs_2024_25.signup.SignUpStudentActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

            getUserIdByEmail(email);
        });

        signupRedirectText.setOnClickListener(v ->
                startActivity(new Intent(LogInStudentActivity.this, SignUpStudentActivity.class))
        );
    }

    private void getUserIdByEmail(String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Students")
                .whereEqualTo("name", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                        Number userId = document.getLong("userID");

                        if (userId != null) {
                            int userIdInt = userId.intValue();
                            Intent intent = new Intent(LogInStudentActivity.this, NfcCardEmulationActivity.class);
                            intent.putExtra("USER_ID", userIdInt);
                            startActivity(intent);
                        } else {
                            loginEmail.setError("User ID not found");
                        }
                    } else {
                        loginEmail.setError("Invalid email");
                    }
                })
                .addOnFailureListener(e -> {
                    loginEmail.setError("Error searching for user");
                });
    }
}