package com.example.firebaseauthenticationtest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.UploadTask.TaskSnapshot;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URI;


public class ProfileActivity extends AppCompatActivity {

    private static final int A_CONSTANT = 420;
    EditText profileName, profilePassword, profileEmail;
    ImageView profilePic;
    Uri uriProfilePic;
    ProgressBar progressBar;

    String profilePicUrl;
    FirebaseAuth mAuth;
    DatabaseReference ref;

    String passedUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Users");

        profileName = (EditText) findViewById(R.id.textUsername);
        profilePassword = (EditText) findViewById(R.id.textPassword);
        profileEmail = (EditText) findViewById(R.id.textEmail);
        profilePic = (ImageView) findViewById(R.id.imageProfile);
        progressBar = findViewById(R.id.progressBar);

        passedUsername = getIntent().getExtras().getString("arg");
        loadUserInformation();



        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });

        findViewById(R.id.buttonUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInformation();
            }
        });
    }


    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() == null){
            //handle already registered user
            //finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == A_CONSTANT && resultCode == RESULT_OK && data != null && data.getData() != null){
            uriProfilePic = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfilePic);
                profilePic.setImageBitmap(bitmap);
                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadUserInformation(){

        progressBar.setVisibility(View.VISIBLE);

        StorageReference profilePicRef = FirebaseStorage.getInstance().getReference("defaultUser" + ".png");
        profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("URI", uri.toString());
                Glide.with(ProfileActivity.this).load(uri).into(profilePic);
                progressBar.setVisibility(View.GONE);
                //url = uri.toString();

            }
        });

        ref.child(passedUsername).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                profileName.setText(user.getUserName());
                profileEmail.setText(user.getEmail());
                profilePassword.setText(user.getPassword());

            }

            public void onCancelled(DatabaseError databaseError) {
                //System.out.println("The read failed: " + databaseError.getCode());
                Toast.makeText(getApplicationContext(), databaseError.getCode(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateUserInformation(){
        String u_name = profileName.getText().toString().trim();
        String pwd = profilePassword.getText().toString().trim();
        String mail = profileEmail.getText().toString().trim();

        if(u_name.isEmpty()){
            profileName.setError("Username is required");
            profileName.requestFocus();
            return;
        }

        if(pwd.isEmpty()){
            profilePassword.setError("Password is required");
            profilePassword.requestFocus();
            return;
        }
        if(pwd.length() < 6){
            profilePassword.setError("Password must be of length 06 or more");
            profilePassword.requestFocus();
            return;
        }

        if(mail.isEmpty()){
            profileEmail.setError("Email is required");
            profileEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            profileEmail.setError("Please enter a valid email");
            profileEmail.requestFocus();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null && profilePicUrl != null){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(null)
                    .setPhotoUri(Uri.parse(profilePicUrl))
                    .build();
            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ProfileActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


    }

    private void uploadImageToFirebaseStorage() {
        final StorageReference profilePicRef =
                FirebaseStorage.getInstance().getReference(passedUsername + ".jpg");
        if(uriProfilePic != null){
            //progressBar.setVisibility(View.VISIBLE);
            profilePicRef.putFile(uriProfilePic).addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
                @Override
                public void onSuccess(TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    profilePicUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                }
            })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        }
    }

    public void showImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), A_CONSTANT);
    }
}
