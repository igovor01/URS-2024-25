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
}
