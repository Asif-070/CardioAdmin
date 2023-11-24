package com.example.cardioadmin;

import static android.text.TextUtils.isEmpty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddActivity extends AppCompatActivity {

    EditText e1,e2,e3,e4,em,ep,es,ed;
    ImageView img;
    Button b1,b2;
    Uri imguri;
    //String name, visit, time, room;

    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Doctor");
    final private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        e1 = findViewById(R.id.editName);
        e2 = findViewById(R.id.editVisit);
        e3 = findViewById(R.id.editTime);
        e4 = findViewById(R.id.editRoom);
        em = findViewById(R.id.editEmail);
        ep = findViewById(R.id.editPass);
        es = findViewById(R.id.editSpc);
        ed = findViewById(R.id.editDay);
        b1 = findViewById(R.id.chooseImageButton);
        b2 = findViewById(R.id.updateButton);
        img = findViewById(R.id.imageView);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the file picker when the "Choose Image" button is clicked
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values from EditText fields
                String name = e1.getText().toString();
                String visit = e2.getText().toString();
                String time = e3.getText().toString();
                String room = e4.getText().toString();
                String email = em.getText().toString();
                String pass = ep.getText().toString();
                String spc = es.getText().toString();

                // Check if any information is missing
                if (name.isEmpty() || visit.isEmpty() || time.isEmpty() || room.isEmpty() || email.isEmpty() || pass.isEmpty() || spc.isEmpty() ||imguri == null) {
                    StringBuilder errorMessage = new StringBuilder("Please fill in the field ");

                    if (name.isEmpty()) {
                        errorMessage.append("Name");
                    }
                    else if (email.isEmpty()) {
                        errorMessage.append("- Email\n");
                    }
                    else if (!isValidEmail(email)) {
                        errorMessage.append("- Valid Email\n");
                    }
                    else if (pass.isEmpty()) {
                        errorMessage.append("- Password\n");
                    }
                    else if (visit.isEmpty()) {
                        errorMessage.append("Visit");
                    }
                    else if (time.isEmpty()) {
                        errorMessage.append("Time");
                    }
                    else if (room.isEmpty()) {
                        errorMessage.append("Room");
                    }
                    else if (spc.isEmpty()) {
                        errorMessage.append("Expertise");
                    }
                    else if (imguri == null) {
                        errorMessage.append("Image");
                    }

                    Toast.makeText(AddActivity.this, errorMessage.toString(), Toast.LENGTH_LONG).show();
                }
                else {
                        final String doctorId = email.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
                        final String finalDoctorId = doctorId.substring(0, doctorId.length() - 3);

                        // Check if the ID (email) already exists in the database
                        DatabaseReference doctorRef = databaseReference.child(finalDoctorId);
                        doctorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // If the ID already exists, show a toast message
                                    Toast.makeText(AddActivity.this, "Email Already Exists\nPlease enter a new email", Toast.LENGTH_SHORT).show();
                                } else {
                                    // If the ID doesn't exist, proceed with uploading the data
                                    uploadToFirebase();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle database error if needed
                            }
                        });
                    }
                }
        });
    }

    private boolean isValidEmail(CharSequence target) {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    // Handle the result of the file picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imguri = data.getData();
            img.setImageURI(imguri);
        }
        else {
            Toast.makeText(AddActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    // Upload data to Firebase
    private void uploadToFirebase() {
        // Get values from EditText fields
        String name = e1.getText().toString();
        int visit = Integer.parseInt(e2.getText().toString());
        String time = e3.getText().toString();
        String room = e4.getText().toString();
        String day = ed.getText().toString();
        String email = em.getText().toString();
        String pass = ep.getText().toString();
        String spc = es.getText().toString();
        String doctorId = email.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        doctorId = doctorId.substring(0, doctorId.length() - 3);

        // Upload image to Firebase Storage
        StorageReference imageReference = storageReference.child("doctor/" + doctorId + ".jpg");
//        imageReference.putFile(imguri);

        String finalDoctorId = doctorId;
        imageReference.putFile(imguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Dataclass dataClass = new Dataclass(finalDoctorId, name, time, room, day, email, pass, spc, uri.toString(), visit, 0, "...", "...", "Nil", "...", "...");
                        databaseReference.child(finalDoctorId).setValue(dataClass);
                        Toast.makeText(AddActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        // Create a Doctor object
//        Dataclass doctor = new Dataclass(name, time, room, doctorId, pass, imageReference.toString(), visit, 0, "...", "...", "...", "...", "...");

        // Add Doctor object to Firebase Database
//        databaseReference.child(doctorId).setValue(doctor);

        // Display success message
        Toast.makeText(AddActivity.this, "Doctor information added successfully", Toast.LENGTH_SHORT).show();

        // Finish the activity
        finish();
    }

}