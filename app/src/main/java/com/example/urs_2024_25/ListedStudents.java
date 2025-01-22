package com.example.urs_2024_25;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import android.util.Log;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ListedStudents extends AppCompatActivity {
    // List that holds student information
    ArrayList<ListData> dataArrayList = new ArrayList<>();
    // LitAdapter that connects the data to the ListView
    LitAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listed_students); // Screen uses the listed_students.xml file to define its design

        ListView listView = findViewById(R.id.listview);

        Users users = new Users();
        users.loadStudentsBasedOnAttendance(new Users.DataCallback() {
            @Override
            public void onDataLoaded(List<UserModel> usersData) {
                if(usersData.isEmpty()) {
                    Toast.makeText(ListedStudents.this, "0 students attended this class!", Toast.LENGTH_SHORT).show();
                } else {
                    int imageRes = R.drawable.profile; // Everyone has the same profile image

                    // Populate the dataArrayList
                    for (UserModel user : usersData) {
                        dataArrayList.add(new ListData(user.getTimeStamp(), imageRes, user.getName(), user.getSurname()));
                    }

                    // Set up the adapter with the loaded data
                    listAdapter = new LitAdapter(ListedStudents.this, dataArrayList);
                    listView.setAdapter(listAdapter);
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(ListedStudents.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        }, 1L);
    }
}
