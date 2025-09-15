package com.example.cardioadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddAmbActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    amb_adapter adapter;
    List<amb_items> items = new ArrayList<amb_items>();
    Button btn;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_amb);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        btn = findViewById(R.id.button);
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new amb_adapter(this, items);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseDatabase.getInstance().getReference("Amb");

        // Fetch data from Firebase and populate the RecyclerView
        fetchDataFromFirebase();

        adapter.setOnDeleteItemClickListener(new amb_adapter.OnDeleteItemClickListener() {
            @Override
            public void onDeleteItemClick(final int position) {
                // Show delete confirmation dialog
                showDeleteConfirmationDialog(position);
            }
        });

//        items.add(new amb_items("Aspirin","Pain relief, anti-inflammatory, and anti-fever."));
//        items.add(new amb_items("Paracetamol","Pain relief and fever reduction"));
//        items.add(new amb_items("Zantac","Heartburn and acid indigestion"));
//        items.add(new amb_items("Metformin","Type 2 diabetes management"));
//        items.add(new amb_items("Effexor","Treatment of depression and anxiety disorders"));

        adapter.notifyDataSetChanged();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddAmbActivity.this, AmbActivity.class);
                startActivity(intent);
            }
        });

    }

    private void fetchDataFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear(); // Clear existing items

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Retrieve data and add it to the list
                    String name = snapshot.child("name").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);

                    items.add(new amb_items(name, phone));
                }

                adapter.notifyDataSetChanged(); // Notify adapter of data change
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
                progressDialog.dismiss();
            }
        });
    }
    private void deleteItem(int position) {
        // Get the selected item
        amb_items selectedItem = items.get(position);

        // Remove item from RecyclerView
        items.remove(position);
        adapter.notifyItemRemoved(position);

        // Remove item from Firebase Database using the selected item's unique identifier (assuming you have such an identifier)
        databaseReference.child(selectedItem.getName()).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Item successfully deleted from the database
                        Toast.makeText(AddAmbActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to delete item
                        Toast.makeText(AddAmbActivity.this, "Failed to delete item", Toast.LENGTH_SHORT).show();
                        // If deletion from Firebase fails, add the item back to the list
                        items.add(position, selectedItem);
                        adapter.notifyItemInserted(position);
                    }
                });
    }

    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Call the deleteItem function when confirmation is positive
                        deleteItem(position);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss the dialog if canceled
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

}