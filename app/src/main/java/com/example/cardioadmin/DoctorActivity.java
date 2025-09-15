package com.example.cardioadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DoctorActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<doctorlist> dataList;
    ProgressDialog progressDialog;
    SearchView searchView;
    doctoradapter adapter;
    DatabaseReference databaseReference;
    String item;
    //    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Doctor");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..."); // Set your desired message here
        progressDialog.setCancelable(false); // Set if the dialog is cancelable or not
        progressDialog.show();

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterText(newText);
                return true;
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        dataList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new doctoradapter(this, dataList);
        recyclerView.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    dataList.clear(); // Clear existing data before adding new data

                    // Iterate through the data snapshot to retrieve doctor information
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Assuming your doctorlist class has appropriate constructor and getter methods
                        doctorlist doctor = snapshot.getValue(doctorlist.class);
                        dataList.add(doctor); // Add the retrieved doctor information to your list
                    }

                    adapter.notifyDataSetChanged(); // Notify adapter of data changes
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors when fetching data
                Toast.makeText(DoctorActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void filterText(String text) {
        ArrayList<doctorlist> filteredList = new ArrayList<>();

        for (doctorlist doctor : dataList) {
            // Here, you can define your filtering logic
            // For instance, if you want to filter by patient name
            if (doctor.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(doctor);
            }
        }

        if (filteredList.isEmpty()) {
            adapter.setSearchList(new ArrayList<>());
//            Toast.makeText(this, "No matching results found", Toast.LENGTH_SHORT).show();
        }
        else{
            // Pass the filtered list to the adapter
            adapter.setSearchList(filteredList);
        }
    }
}