package com.example.cardioadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientViewActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    CircleImageView profile;
    CardView c1;
    ImageView back;
    String uid;

    private TextView name, phone, type, age, blood, gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view);

        name = findViewById(R.id.tieName);
        phone = findViewById(R.id.phone);
        type = findViewById(R.id.type);
        age = findViewById(R.id.age);
        blood = findViewById(R.id.blood);
        gender = findViewById(R.id.gender);
        back = findViewById(R.id.backbtn);
        c1 = findViewById(R.id.delete);

        profile = findViewById(R.id.imageView4);

        // Get the doctor ID passed from the previous activity
        String patientID = getIntent().getStringExtra("PatientID");

        // Firebase reference to doctors node
        databaseReference = FirebaseDatabase.getInstance().getReference().child("patient").child(patientID);

        // Fetch data for the specific doctor
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve doctor information
                    String name2 = dataSnapshot.child("name").getValue().toString();
                    int age2 = Integer.parseInt(dataSnapshot.child("age").getValue().toString());
                    String gender2 = dataSnapshot.child("gender").getValue().toString();
                    String type2 = dataSnapshot.child("type").getValue().toString();
                    String phone2 = dataSnapshot.child("phone").getValue().toString();
                    String blood2 = dataSnapshot.child("bt").getValue().toString();
                    String imageUrl = dataSnapshot.child("imgurl").getValue().toString();
                    uid = dataSnapshot.child("uid").getValue().toString();

                    // Set the fetched data to TextViews
                    name.setText(name2);
                    type.setText("Type: " + type2);
                    age.setText("Age: " + age2);
                    blood.setText("Blood: " + blood2);
                    gender.setText("Gender: " + gender2);
                    phone.setText("Phone: " + phone2);

                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        // Use your logic to load the image using Glide
                        Glide.with(PatientViewActivity.this)
                                .load(imageUrl)
                                .placeholder(R.drawable.editprofile)
                                .into(profile);
                    } else {
                        // Use a placeholder image if the URL is empty
                        profile.setImageResource(R.drawable.editprofile);
                    }
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors when fetching data
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Finish the current activity
            }
        });

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an alert dialog for confirmation
                new AlertDialog.Builder(PatientViewActivity.this)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to delete this record?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // User confirmed deletion, proceed with removing the data
                                databaseReference.removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Data has been deleted successfully
                                                // Redirect to a different activity or perform necessary actions
                                                Intent intent = new Intent(PatientViewActivity.this, DoctorActivity.class);
                                                startActivity(intent);
                                                finish(); // Finish the current activity
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Handle any errors that may occur during the deletion process
                                                Toast.makeText(PatientViewActivity.this, "Failed to delete data", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // User cancelled the deletion
                                // Dismiss the dialog or handle accordingly
                                dialog.dismiss();
                            }
                        })
                        .show(); // Display the dialog
            }
        });

    }
}