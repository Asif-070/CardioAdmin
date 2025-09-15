package com.example.cardioadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class DoctorViewActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    CircleImageView profile;
    CardView c1,c2;
    ImageView back;
    String phone2;
    private TextView name, exp, age, visit, gender, room, time, about, day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view);

        name = findViewById(R.id.tieName);
        exp = findViewById(R.id.exp);
        age = findViewById(R.id.age);
        visit = findViewById(R.id.visit);
        gender = findViewById(R.id.gender);
        room = findViewById(R.id.room);
        day = findViewById(R.id.day);
        time = findViewById(R.id.time);
        about = findViewById(R.id.about);
        back = findViewById(R.id.backbtn);
        c1 = findViewById(R.id.edit);
        c2 = findViewById(R.id.delete);

        profile = findViewById(R.id.imageView4);

        // Get the doctor ID passed from the previous activity
        String doctorID = getIntent().getStringExtra("DoctorID");

        // Firebase reference to doctors node
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Doctor").child(doctorID);

        // Fetch data for the specific doctor
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve doctor information
                    String name2 = dataSnapshot.child("name").getValue().toString();
                    int age2 = Integer.parseInt(dataSnapshot.child("age").getValue().toString());
                    String gender2 = dataSnapshot.child("gender").getValue().toString();
                    String exp2 = dataSnapshot.child("exp").getValue().toString();
                    String room2 = dataSnapshot.child("room").getValue().toString();
                    String day2 = dataSnapshot.child("day").getValue().toString();
                    String time2 = dataSnapshot.child("time").getValue().toString();
                    int visit2 = Integer.parseInt(dataSnapshot.child("visit").getValue().toString());
                    String imageUrl = dataSnapshot.child("imgurl").getValue().toString();

                    // Set the fetched data to TextViews
                    name.setText(name2);
                    about.setText("About " + name2 + ":");
                    exp.setText("Experience: " + exp2 + " Years");
                    age.setText("Age: " + age2);
                    visit.setText("Visit: " + visit2 + " TK");
                    gender.setText("Gender: " + gender2);
                    room.setText("Room: " + room2);
                    day.setText("Day: " + day2);
                    time.setText("Time: " + time2);

                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        // Use your logic to load the image using Glide
                        Glide.with(DoctorViewActivity.this)
                                .load(imageUrl)
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
                Intent intent = new Intent(DoctorViewActivity.this, DeditActivity.class);
                intent.putExtra("DoctorID", doctorID);
                startActivity(intent);
            }
        });

        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an alert dialog for confirmation
                new AlertDialog.Builder(DoctorViewActivity.this)
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
                                                Intent intent = new Intent(DoctorViewActivity.this, DoctorActivity.class);
                                                startActivity(intent);
                                                finish(); // Finish the current activity
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Handle any errors that may occur during the deletion process
                                                Toast.makeText(DoctorViewActivity.this, "Failed to delete data", Toast.LENGTH_SHORT).show();
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