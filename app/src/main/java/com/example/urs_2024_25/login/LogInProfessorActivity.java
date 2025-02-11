package com.example.urs_2024_25.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.urs_2024_25.R;
import com.example.urs_2024_25.nfc.reader.NFCReaderActivity2;
import com.example.urs_2024_25.signup.SignUpProfessorActivity;
import com.example.urs_2024_25.nfc.reader.NFCReaderActivity;

public class LogInProfessorActivity extends AppCompatActivity {

    private EditText loginEmail, loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in_professor);

        loginEmail = findViewById(R.id.login_professor_email);
        loginPassword = findViewById(R.id.login_professor_password);
        TextView signupRedirectText = findViewById(R.id.signup_professor_redirect);
        Button loginButton = findViewById(R.id.login_professor_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString();
                String pass = loginPassword.getText().toString();

                if (email.isEmpty()) {
                    loginEmail.setError("Email cannot be empty");
                }
                if (pass.isEmpty()) {
                    loginPassword.setError("Password cannot be empty");
                }

                startActivity(new Intent(LogInProfessorActivity.this, NFCReaderActivity2.class));
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInProfessorActivity.this, SignUpProfessorActivity.class));
            }
        });
    }
}