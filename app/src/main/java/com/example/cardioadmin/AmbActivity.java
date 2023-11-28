package com.example.cardioadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AmbActivity extends AppCompatActivity {

    EditText e1,e2;
    Button b1;
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Amb");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amb);

        e1 = findViewById(R.id.editName);
        e2 = findViewById(R.id.editPhone);
        b1 = findViewById(R.id.updateButton);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values from EditText fields
                String name = e1.getText().toString();
                String phn = e2.getText().toString();

                // Check if any information is missing
                if (name.isEmpty() || phn.isEmpty()) {
                    StringBuilder errorMessage = new StringBuilder("Please fill in the field ");

                    if (name.isEmpty()) {
                        errorMessage.append("Name");
                    }
                    else if (phn.isEmpty()) {
                        errorMessage.append("- Phone no\n");
                    }

                    Toast.makeText(AmbActivity.this, errorMessage.toString(), Toast.LENGTH_LONG).show();
                }
                else {
                    // Create a unique key for the new entry in the "Amb" node
                    String newEntryKey = name;

                    // Create a HashMap to hold the data
                    amb_items dataClass = new amb_items(name, phn);


                    // Push the data to the Firebase Database under "Amb" node with the unique key
                    if (newEntryKey != null) {
                        databaseReference.child(newEntryKey).setValue(dataClass)
                                .addOnSuccessListener(aVoid -> {
                                    // Data successfully added
                                    Toast.makeText(AmbActivity.this, "Data added to Firebase", Toast.LENGTH_SHORT).show();
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    // Failed to add data
                                    Toast.makeText(AmbActivity.this, "Failed to add data to Firebase", Toast.LENGTH_SHORT).show();
                                });
                    }
                }
            }
        });
    }
}