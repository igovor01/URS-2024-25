package com.example.urs_2024_25.signup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.urs_2024_25.Attendance;
import com.example.urs_2024_25.R;
import com.example.urs_2024_25.liststudents.UserModel;
import com.example.urs_2024_25.login.LogInStudentActivity;
import com.example.urs_2024_25.nfc.reader.NFCReaderActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SignUpStudentActivity extends AppCompatActivity implements Users2.DataCallback {

    public static final String TAG = SignUpStudentActivity.class.getSimpleName();
    public static final int REQUEST_CODE = 123;
    private EditText signupName, signupSurname, signupPassword;

    private Users2 users;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_student);

        signupName = findViewById(R.id.signup_student_name);
        signupSurname = findViewById(R.id.signup_student_surname);
        signupPassword = findViewById(R.id.signup_student_password);
        Button cardReaderButton = findViewById(R.id.card_reader_button);
        Button signupButton = findViewById(R.id.signup_student_button);
        TextView loginRedirectText = findViewById(R.id.login_student_redirect);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        users = new Users2(db, this);

        cardReaderButton.setOnClickListener(v -> startActivityForResult(
                new Intent(SignUpStudentActivity.this,
                        NFCReaderActivity.class), REQUEST_CODE));

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

            users.recordUser("ivanaa", "goo", 123);
        });

        loginRedirectText.setOnClickListener(v -> startActivity(
                new Intent(SignUpStudentActivity.this,
                        LogInStudentActivity.class)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == NFCReaderActivity.RESULT_OK) {
            var userId = data.getIntExtra("USER_ID", -1);
            Toast.makeText(this, "Got Result: "+userId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccess(String message) {
        runOnUiThread(() -> {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onError(String message) {
        runOnUiThread(() -> {
            Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "User error: " + message);
        });
    }
}