//pripremi podatke od kojih ce
//za svakog studenta kreirat listData objekt,
//te listData objekte salje ListAdapteru i onda listAdapter prikaze te podatke
package com.example.urs_2024_25;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListedStudents extends AppCompatActivity {
    //list that holds student information (
    ArrayList<ListData> dataArrayList = new ArrayList<>();
    //LitAdapter that connects the data to the ListView
    LitAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listed_students); //screen uses the listed_students.xml file to define its design

        ListView listView = findViewById(R.id.listview);

        int[] imageList = {R.drawable.profile};//everyone has same profile image
        String[] nameList = {"Ime1", "Ime2", "Ime3"};
        String[] timeList = {"2025-01-20 14:47", "2025-01-20 14:48", "2025-01-20 14:49"};

        for (int i = 0; i < nameList.length; i++) {
            //ListData object is created with their name, time, and image
            dataArrayList.add(new ListData(timeList[i], imageList[0], nameList[i]));
        }

        //new LitAdapter is created, connecting the data to the screen
        listAdapter = new LitAdapter(this, dataArrayList);
        listView.setAdapter(listAdapter);
    }
}
