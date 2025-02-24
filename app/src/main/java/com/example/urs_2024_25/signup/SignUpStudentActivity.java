package com.example.urs_2024_25.signup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.urs_2024_25.R;
import com.example.urs_2024_25.login.LogInStudentActivity;

public class SignUpStudentActivity extends AppCompatActivity {

    private EditText signupName, signupSurname, signupPassword;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_student);

        signupName = findViewById(R.id.signup_student_name);
        signupSurname = findViewById(R.id.signup_student_surname);
        signupPassword = findViewById(R.id.signup_student_password);
        Button signupButton = findViewById(R.id.signup_student_button);
        TextView loginRedirectText = findViewById(R.id.login_student_redirect);


        signupButton.setOnClickListener(v -> {
            String name = signupName.getText().toString().trim();
            String surname = signupSurname.getText().toString().trim();
            String pass = signupPassword.getText().toString().trim();

            if (name.isEmpty()) {
                signupName.setError("Name cannot be empty");
            }
            if (surname.isEmpty()) {
                signupSurname.setError("Surname cannot be empty");
            }
            if (pass.isEmpty()) {
                signupPassword.setError("Password cannot be empty");
            }
        });

        loginRedirectText.setOnClickListener(v -> startActivity(
                new Intent(SignUpStudentActivity.this,
                        LogInStudentActivity.class)));
    }
}