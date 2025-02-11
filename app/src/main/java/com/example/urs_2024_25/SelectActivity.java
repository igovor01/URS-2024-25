package com.example.urs_2024_25;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.urs_2024_25.login.LogInProfessorActivity;
import com.example.urs_2024_25.login.LogInStudentActivity;

public class SelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select);

        Button studentButton = findViewById(R.id.student_button);
        Button professorButton = findViewById(R.id.professor_button);


        studentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectActivity.this, LogInStudentActivity.class));
            }
        });

        professorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectActivity.this, LogInProfessorActivity.class));
            }
        });
    }
}