package com.example.urs_2024_25;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView dataTextView;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        dataTextView = findViewById(R.id.dataTextView);

        /* kreiranje korisnika
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });*/
        readFirestoreData();
    }

    private void readFirestoreData() {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            StringBuilder data = new StringBuilder();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Append each user's data to the StringBuilder
                                data.append("User ID: ").append(document.getId())
                                        .append("\nFirst Name: ").append(document.getString("first"))
                                        .append("\nLast Name: ").append(document.getString("last"))
                                        .append("\nBorn: ").append(document.getLong("born"))
                                        .append("\n\n");
                            }
                            // Set the text to the TextView
                            dataTextView.setText(data.toString());
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                            dataTextView.setText("Error loading data");
                        }
                    }
                });
    }
}