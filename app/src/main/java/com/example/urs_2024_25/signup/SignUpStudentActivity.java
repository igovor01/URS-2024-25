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

import com.example.urs_2024_25.R;
import com.example.urs_2024_25.liststudents.UserModel;
import com.example.urs_2024_25.liststudents.Users;
import com.example.urs_2024_25.login.LogInStudentActivity;
import com.example.urs_2024_25.nfc.cardreader.NfcCardReaderService;
import java.util.List;

public class SignUpStudentActivity extends AppCompatActivity {

    private static final String TAG = SignUpStudentActivity.class.getSimpleName();

    private EditText signupName, signUpSurname, signupPassword;
    private Button cardReaderButton = findViewById(R.id.card_reader_button);

    private int userId;
    //private Users users;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_student);

        //users = new Users();

        signupName = findViewById(R.id.signup_student_name);
        signUpSurname = findViewById(R.id.signup_student_surname);
        signupPassword = findViewById(R.id.signup_student_password);
        Button signupButton = findViewById(R.id.signup_student_button);
        TextView loginRedirectText = findViewById(R.id.login_student_redirect);

//        cardReaderButton.setOnClickListener(view -> {
//            startCardReading();
//        });


        signupButton.setOnClickListener(v -> {
            //if(!signupButton.isEnabled()){
            //    return;
            //}

            String name = signupName.getText().toString().trim();
            String surname = signUpSurname.getText().toString().trim();
            String pass = signupPassword.getText().toString().trim();

            if (name.isEmpty()) {
                signupName.setError("Name cannot be empty");
            }
            if (surname.isEmpty()) {
                signUpSurname.setError("Surname cannot be empty");
            }
            if (pass.isEmpty()) {
                signupPassword.setError("Password cannot be empty");
            }

//            users.recordUser(new Users.DataCallback() {
//                @Override
//                public void onDataLoaded(List<UserModel> usersData) {
//                }
//
//                @Override
//                public void onError(String errorMessage) {
//                    runOnUiThread(() -> {
//                        Toast.makeText(SignUpStudentActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
//                        Log.e(TAG, "User error: " + errorMessage);
//                    });
//                }
//
//                @Override
//                public void onSuccess(String message) {
//                    runOnUiThread(() -> {
//                        Toast.makeText(SignUpStudentActivity.this, message, Toast.LENGTH_SHORT).show();
//                    });
//
//                }
//            }, 1234567, name, surname);
        });

        loginRedirectText.setOnClickListener(v ->
                startActivity(new Intent(SignUpStudentActivity.this, LogInStudentActivity.class)));
    }

//    private void startCardReading() {
//        Intent intent = new Intent(SignUpStudentActivity.this, NfcCardReaderService.class);
//        startService(intent);
//    }
}