package com.example.cardioadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DeditActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    EditText editPass, editVisit, editTime, editRoom,editDay, editSpc,name,email,age,phn;
    ImageView editimg;
    StorageReference storageReference;
    Uri imguri;
    String doctorID;
    Button updateButton,imgButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dedit);

        doctorID = getIntent().getStringExtra("DoctorID");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Doctor").child(doctorID);

        name = findViewById(R.id.editName);
        email = findViewById(R.id.editEmail);
        age = findViewById(R.id.editAge);
        phn = findViewById(R.id.editPhone);
        editPass = findViewById(R.id.editPass);
        editVisit = findViewById(R.id.editVisit);
        editTime = findViewById(R.id.editTime);
        editRoom = findViewById(R.id.editRoom);
        editDay = findViewById(R.id.editDay);
        editSpc = findViewById(R.id.editSpc);
        editimg = findViewById(R.id.imageView);
        imgButton = findViewById(R.id.chooseImageButton);
        updateButton = findViewById(R.id.updateButton);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    doctor p = dataSnapshot.getValue(doctor.class); // Create a User class that matches your database structure
                    // Update UI with user data
                    name.setText(String.valueOf(p.getName()));
                    email.setText(String.valueOf(p.getEmail()));
                    age.setText(String.valueOf(p.getAge()));
                    phn.setText(String.valueOf(p.getPhone()));
                    editPass.setText(String.valueOf(p.getPass()));
                    editVisit.setText(String.valueOf(p.getVisit()));
                    editTime.setText(String.valueOf(p.getTime()));
                    editRoom.setText(String.valueOf(p.getRoom()));
                    editDay.setText(String.valueOf(p.getDay()));
                    editSpc.setText(String.valueOf(p.getSpc()));
                    String imageUrl = String.valueOf(p.getImgurl());

                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        // Load image into ImageView using Glide
                        Glide.with(DeditActivity.this)
                                .load(imageUrl)
                                .into(editimg);
                    } else {
                        // Use a placeholder image if no image URL is available
                        editimg.setImageResource(R.drawable.editprofile);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, 1);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imguri = data.getData();
            editimg.setImageURI(imguri);
        }
        else {
            Toast.makeText(DeditActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProfile() {
        // Update the user profile in Firebase
        databaseReference.child("pass").setValue(editPass.getText().toString());
        databaseReference.child("visit").setValue(Integer.parseInt(editVisit.getText().toString()));
        databaseReference.child("time").setValue(editTime.getText().toString());
        databaseReference.child("room").setValue(editRoom.getText().toString());
        databaseReference.child("day").setValue(editDay.getText().toString());
        databaseReference.child("spc").setValue(editSpc.getText().toString());

        if (imguri != null) {
            // Make sure storageReference is initialized
            storageReference = FirebaseStorage.getInstance().getReference(); // Initialize storage reference here

            StorageReference imageReference = storageReference.child("doctor/" + doctorID + ".jpg");

            imageReference.putFile(imguri).addOnSuccessListener(taskSnapshot -> {
                // Get the download URL of the uploaded image
                imageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Store the image URL in the database
                    databaseReference.child("imgurl").setValue(uri.toString());

                    Toast.makeText(DeditActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                    // Finish the activity or navigate back to the profile page
                    Intent intent = new Intent(DeditActivity.this, DoctorViewActivity.class);
                    startActivity(intent);
                    finish();
                }).addOnFailureListener(e ->
                        Toast.makeText(DeditActivity.this, "Failed to get image URL", Toast.LENGTH_SHORT).show());
            }).addOnFailureListener(e ->
                    Toast.makeText(DeditActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show());
        } else {
            // If no image is selected, proceed without uploading an image
            Toast.makeText(DeditActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

            // Finish the activity or navigate back to the profile page
            Intent intent = new Intent(DeditActivity.this, DoctorViewActivity.class);
            startActivity(intent);
            finish();
        }

    }
}

