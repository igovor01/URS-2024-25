//dobije listu ListData objekata, za svakog studenta postavi ime sliku i vrime
//prikaze sve te studente
package com.example.urs_2024_25;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

//ArrayAdapter  helps connect data to a list,
//using it to display ListData objects (data for one student)

public class LitAdapter extends ArrayAdapter<ListData> {
    public LitAdapter(@NonNull Context context, ArrayList<ListData> dataArrayList) {
        super(context, R.layout.list_students, dataArrayList);
        //which layout to use for each row (R.layout.list_students
    }

    @NonNull
    @Override
    //for each item in the dataset creates and returns the corresponding row view
    //int position- index of the current item in the dataset
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        ListData listData = getItem(position); //find the student data for the current row using the position

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_students, parent, false);
        }//create a new row by loading the list_students.xml layout file

        //findViewById()- to locate the UI elements in the list_students layout
        ImageView listImage = view.findViewById(R.id.listImage);
        TextView listName = view.findViewById(R.id.listName);
        TextView listSurname = view.findViewById(R.id.listSurname);
        TextView listTime = view.findViewById(R.id.listTime);

        //fill the image and text with the current student’s data
        listImage.setImageResource(listData.image);
        listName.setText(listData.name);
        listSurname.setText(listData.surname);
        listTime.setText(listData.time);

        return view;
    }
}
