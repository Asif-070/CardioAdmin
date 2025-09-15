package com.example.cardioadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class AddappActivity extends AppCompatActivity {

    EditText patientEmailEditText;
    Button patientCheckButton,addButton;
    TextView nameTextView, ageTextView, phoneTextView, docTextView, spcTextView, selectedDateTextView;
    Calendar calendar;
    private ArrayAdapter<String> doctorAdapter;
    private DatabaseReference databaseReference;
    String pe = "",ad = "", de = "", dn = "", pn = "", gender = "";
    Spinner doctorSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addapp);

        patientEmailEditText = findViewById(R.id.patient);
        patientCheckButton = findViewById(R.id.patientButton);
        doctorSpinner = findViewById(R.id.spinnerdoc);
        nameTextView = findViewById(R.id.namep);
        ageTextView = findViewById(R.id.agep);
        phoneTextView = findViewById(R.id.phnp);

        docTextView = findViewById(R.id.named);
        spcTextView = findViewById(R.id.spcd);

        selectedDateTextView = findViewById(R.id.selectedDate);
        Button dateButton = findViewById(R.id.dateButton);

        addButton = findViewById(R.id.updateButton);

        databaseReference = FirebaseDatabase.getInstance().getReference("Doctor");

        // Create an ArrayAdapter for the Spinner
        doctorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        doctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctorSpinner.setAdapter(doctorAdapter);

        // Fetch doctors from Firebase and populate the Spinner
        fetchDoctors();

        // Set a listener for Spinner item selection
        doctorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDoctor = parent.getItemAtPosition(position).toString();
                if(selectedDoctor.equals("Null")){
                    nameTextView.setText("Name: ");
                    spcTextView.setText("Spc: ");
                }
                else{
                    // Retrieve details for the selected doctor and display
                    displayDoctorDetails(selectedDoctor);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle when no doctor is selected
                nameTextView.setText("Name: ");
                spcTextView.setText("Spc: ");
            }
        });

        patientCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String patientEmail = patientEmailEditText.getText().toString().trim();
                // Search Firebase for patient details based on the given email
                searchPatientInFirebase(patientEmail);
            }
        });

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pname = pe;
                String dname = de;
                String date = ad;

                if (pname.isEmpty() || dname.isEmpty() || date.isEmpty()) {
                    StringBuilder errorMessage = new StringBuilder("");

                    if (pname.isEmpty() && !pname.equals("Name: ")) {
                        Toast.makeText(AddappActivity.this, "Please select patient", Toast.LENGTH_LONG).show();
                    }
                    else if (dname.isEmpty()) {
                        Toast.makeText(AddappActivity.this, "Please select doctor", Toast.LENGTH_LONG).show();
                    }
                    else if (date.isEmpty()) {
                        Toast.makeText(AddappActivity.this, "Please select a date", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    uploadToFirebase();
                }
            }
        });

        calendar = Calendar.getInstance();

    }

    private void searchPatientInFirebase(String email) {

        String modifiedEmail = email.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        modifiedEmail = modifiedEmail.substring(0, modifiedEmail.length() - 3);
        // Assuming Firebase database reference is initialized as 'databaseReference'
        DatabaseReference patientRef = FirebaseDatabase.getInstance().getReference("patient").child(modifiedEmail);

        // Read the data at the reference
        String finalModifiedEmail = modifiedEmail;
        patientRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // The patient exists, fetch details and display
                    String name = dataSnapshot.child("name").getValue(String.class);
                    Integer ageInt = dataSnapshot.child("age").getValue(Integer.class);
                    String age = String.valueOf(ageInt);
                    pe = finalModifiedEmail;
                    pn = name;
                    gender = dataSnapshot.child("gender").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);

                    // Display the retrieved information
                    nameTextView.setText("Name: " + name);
                    ageTextView.setText("Age: " + age);
                    phoneTextView.setText("Phone no: " + phone);
                } else {
                    // Handle case when patient does not exist
                    Toast.makeText(AddappActivity.this, "Patient not found", Toast.LENGTH_SHORT).show();
                    nameTextView.setText("Name: ");
                    ageTextView.setText("Age: ");
                    phoneTextView.setText("Phone no: ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(AddappActivity.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchDoctors() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear previous data
                doctorAdapter.clear();
                doctorAdapter.add("Null");
                // Fetch each doctor and add their name to the Spinner
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String doctorName = snapshot.child("name").getValue(String.class);
                    doctorAdapter.add(doctorName);
                }
                doctorAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(AddappActivity.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayDoctorDetails(String selectedDoctorName) {
        databaseReference.orderByChild("name").equalTo(selectedDoctorName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String doctorName = snapshot.child("name").getValue(String.class);
                                dn = doctorName;
                                String doctorSpc = snapshot.child("spc").getValue(String.class);
                                String email = snapshot.child("email").getValue(String.class);
                                String modifiedEmail = email.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
                                de = modifiedEmail.substring(0, modifiedEmail.length() - 3);
                                // Display the retrieved information
                                docTextView.setText("Name: " + doctorName);
                                spcTextView.setText("Spc: " + doctorSpc);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database error
                        Toast.makeText(AddappActivity.this, "Database error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Set the minimum date to today's date
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateSelectedDate();
        }
    };

    private void updateSelectedDate() {
        String dateFormat = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
        ad = simpleDateFormat.format(calendar.getTime());
        selectedDateTextView.setText("Date: " + simpleDateFormat.format(calendar.getTime()));
    }

    private void uploadToFirebase() {
        // Get values from EditText fields
        String pname = pe;
        String dname = de;
        String date = ad;
        String name = dn;
        String paname = pn;
        String gen = gender;

        // Create a unique key for the new appointment
        String appointmentId = FirebaseDatabase.getInstance().getReference().child("appoint").push().getKey();

        // Check if all necessary fields are not empty
        if (!TextUtils.isEmpty(pname) && !TextUtils.isEmpty(dname) && !TextUtils.isEmpty(date)) {
            // Create a HashMap to store appointment details
            HashMap<String, Object> appointmentMap = new HashMap<>();
            appointmentMap.put("patient", pname);
            appointmentMap.put("doctor", dname);
            appointmentMap.put("date", date);
            appointmentMap.put("name", name);
            appointmentMap.put("pname", paname);
            appointmentMap.put("gender", gen);
            appointmentMap.put("id", appointmentId);


            // Add appointment details to Firebase under "appoint" node with the unique appointmentId
            DatabaseReference appointmentRef = FirebaseDatabase.getInstance().getReference().child("appoint").child(appointmentId);
            appointmentRef.setValue(appointmentMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Display success message
                            Toast.makeText(AddappActivity.this, "Appointment added successfully", Toast.LENGTH_SHORT).show();

                            // Finish the activity
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle the error
                            Toast.makeText(AddappActivity.this, "Failed to add appointment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Handle if any of the fields are empty
            Toast.makeText(AddappActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

}